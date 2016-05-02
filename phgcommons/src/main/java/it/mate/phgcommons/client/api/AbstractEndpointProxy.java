package it.mate.phgcommons.client.api;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.PhgUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Window;

/**
 * 
 *  PER L'AUTENTICAZIONE HO SEGUITO I DUE ARTICOLI:
 *  
 *    http://phonegap-tips.com/articles/google-api-oauth-with-phonegaps-inappbrowser.html
 *    http://phonegap-tips.com/articles/oauth-with-phonegaps-inappbrowser-expiration-and-revocation.html
 *  
 *  SEE ALSO:
 *    https://developers.google.com/api-client-library/javascript/reference/referencedocs
 *    https://developers.google.com/+/api/oauth#login-scopes
 *    https://developers.google.com/+/web/signin/sign-out
 *  
 */
public abstract class AbstractEndpointProxy {
  
  protected static final String GOOGLE_CLIENT_API = "https://apis.google.com/js/client.js";
  
  /**
   * SEE https://developers.google.com/+/api/oauth#login-scopes
   */
  private static final String AUTH_SCOPES = "https://www.googleapis.com/auth/userinfo.email";
//private static final String AUTH_SCOPES = "email"; // non funziona!
//private static final String AUTH_SCOPES = "profile"; // non funziona!
//private static final String AUTH_SCOPES = "https://www.googleapis.com/auth/userinfo.profile"; // non funziona!
  
  private String apiRoot;
  
  private String apiName;
  
  private boolean initialized = false;
  
  private boolean signedIn = false;
  
  private Delegate<Void> initDelegate;
  
  private Delegate<Boolean> authDelegate;
  
  private boolean useAuthentication = false;
  
  private final static boolean SIMPLE_AUTH_MODE = false;
  
  private final static int PROXY_BUILD_NUMBER = 103;
  
  protected AbstractEndpointProxy(String apiRoot, String apiName, boolean useAuthentication, Delegate<Void> initDelegate) {
    this(apiRoot, apiName, useAuthentication, initDelegate, null);
  }
  
  protected AbstractEndpointProxy(String apiRoot, String apiName, boolean useAuthentication, Delegate<Void> initDelegate, Delegate<Boolean> authDelegate) {
    super();
    this.apiRoot = apiRoot;
    this.apiName = apiName;
    this.initDelegate = initDelegate;
    this.useAuthentication = useAuthentication;
    this.authDelegate = authDelegate;
    PhgUtils.log("initializing endpoint proxy " + apiName + "...");
    JSONUtils.ensureStringify();
    initClientApi();
  }
  
  protected abstract String getDesktopClientId();
  
  protected abstract String getMobileClientId();
  
  protected abstract String getMobileClientSecret();
  
  protected String getApiKey() {
    return null;
  }
  
  public boolean isInitialized() {
    return initialized;
  }

  public boolean isSignedIn() {
    return signedIn;
  }
  
  public void setAuthDelegate(Delegate<Boolean> authDelegate) {
    this.authDelegate = authDelegate;
  }
  
  protected void initClientApi() {
    createGlobalInitEndpointImpl(this);
    String source = GOOGLE_CLIENT_API+"?onload=_proxyInitEndpointCallback";
    ScriptElement scriptElem = Document.get().createScriptElement();
    scriptElem.setSrc(source);
    scriptElem.setType("text/javascript");
    Element head = Document.get().getElementsByTagName("head").getItem(0);
    head.appendChild(scriptElem);
  }
  
  private native void createGlobalInitEndpointImpl(AbstractEndpointProxy proxy) /*-{
    $wnd._proxyInitEndpointCallback = $entry(function() {
      proxy.@it.mate.phgcommons.client.api.AbstractEndpointProxy::initEndpointApi()();
    });
  }-*/;

  protected void initEndpointApi() {
    PhgUtils.log("calling initEndpointImpl");
    PhgUtils.log("apiRoot = " + apiRoot);
    PhgUtils.log("apiName = " + apiName);
    PhgUtils.log("apiKey = " + getApiKey());
    PhgUtils.log("proxyBuildNm = " + PROXY_BUILD_NUMBER);
    initEndpointApiImpl(apiRoot, apiName, getApiKey(), useAuthentication, new Callback() {
      public void execute(JavaScriptObject proxyRef) {
        if (proxyRef == null) {
          PhgUtils.log("initEndpointApi.callback: ALERT: proxyRef is null!");
        }
        initialized = true;
        onInit();
        if (initDelegate != null)
          initDelegate.execute(null);
        if (useAuthentication) {
          signIn(true);
        }
      }
    });
  }
  
  private native void initEndpointApiImpl(String apiRoot, String apiName, String apiKey, boolean useAuthentication, Callback pCallback) /*-{
    var apisToLoad;
    var callback = function() {
      if (--apisToLoad == 0) {
        $wnd.glbDebugHook($wnd.gapi.client[apiName]);
        var msg = "initEndpointApiImpl: gapi.client = ";
        msg += @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)($wnd.gapi.client);
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)(msg);
        msg = "initEndpointApiImpl: gapi.client["+apiName+"] = ";
        msg += @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)($wnd.gapi.client[apiName]);
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)(msg);
        pCallback.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)($wnd.gapi.client[apiName]);
      }
    }
    if (useAuthentication) {
      apisToLoad = 2; // must match number of calls to gapi.client.load()
    } else {
      apisToLoad = 1; // must match number of calls to gapi.client.load()
    }
    if (apiKey != null) {
      $wnd.gapi.client.setApiKey(apiKey);
    }
    $wnd.gapi.client.load(apiName, 'v1', callback, apiRoot);
    if (useAuthentication) {
      $wnd.gapi.client.load('oauth2', 'v2', callback);
    }
  }-*/;
  
  protected void onInit() {
    
  }
  
  private void signIn(final boolean immediate) {
    
    PhgUtils.log("called signIn " + immediate);
    
    final Callback failure = new Callback() {
      public void execute(JavaScriptObject jso) {
        PhgUtils.log("authorization failure");
      }
    };
    
    if (Window.Navigator.getUserAgent().toLowerCase().contains("windows nt")) {
      
      PhgUtils.log("calling signInDesktopImpl");
      PhgUtils.log("clientId " + getDesktopClientId());
      PhgUtils.log("scopes " + AUTH_SCOPES);
      PhgUtils.log("immediate " + immediate);
      signInDesktopImpl(immediate, getDesktopClientId(), null, AUTH_SCOPES, new Callback() {
        public void execute(JavaScriptObject jso) {
          userAuthedLightImpl(new Callback() {
            public void execute(JavaScriptObject jso) {
              signedIn = true;
              if (authDelegate != null) {
                authDelegate.execute(signedIn);
              }
            }
          });
        }
      }, failure);
      
    } else {
      
      PhgUtils.log("calling signInMobileImpl");
      PhgUtils.log("clientId " + getMobileClientId());
      PhgUtils.log("scopes " + AUTH_SCOPES);
      PhgUtils.log("immediate " + immediate);
      
      final Delegate<Token> validTokenDelegate = new Delegate<Token>() {
        public void execute(Token token) {
          PhgUtils.log("setting gapi auth token");
          setTokenInApiImpl(token);
          PhgUtils.log("calling user authed");
          userAuthedLightImpl(new Callback() {
            public void execute(JavaScriptObject jso) {
              signedIn = true;
              if (authDelegate != null) {
                authDelegate.execute(signedIn);
              }
            }
          });
        }
      };
      
      
      if (SIMPLE_AUTH_MODE) {
        
        /**
         * VECCHIA VERSIONE
         */
        
        signInMobileImpl(immediate, getMobileClientId(), getMobileClientSecret(), AUTH_SCOPES, new Callback() {
          public void execute(JavaScriptObject jso) {
            Token token = jso.cast();
            PhgUtils.log("authorization success with token '" + JSONUtils.stringify(token) + "'");
            validTokenDelegate.execute(token);
          }
        }, failure);
        
      } else {
        
        getTokenFromStorageImpl(getMobileClientId(), getMobileClientSecret(), new Callback() {
          // ON SUCCESS
          public void execute(JavaScriptObject jso) {
            Token token = null;
            PhgUtils.log("FOUND VALID TOKEN IN LOCAL STORAGE");
            token = jso.cast();
            PhgUtils.log("found token '" + JSONUtils.stringify(token) + "'");
            PhgUtils.log("calling valid token delegate");
            validTokenDelegate.execute(token);
          }
        }, new Callback() {
          // ON FAILURE
          public void execute(JavaScriptObject jso) {
            signInMobileImpl(immediate, getMobileClientId(), getMobileClientSecret(), AUTH_SCOPES, new Callback() {
              public void execute(JavaScriptObject data) {
                Token token = data.cast();
                PhgUtils.log("authorization success with token '" + JSONUtils.stringify(token) + "'");
                
                PhgUtils.log("setting token in local storage");
                setTokenInStorageImpl(data);
                
                PhgUtils.log("calling valid token delegate");
                validTokenDelegate.execute(token);

              }
            }, failure);
          }
        });
        
      }
      
    }
    
    
  }
  
  /**
   * ORIGINAL VERSION:
   * 
    if (new Date().getTime() < localStorage.expires_at) {
      success.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(localStorage.token);
    } else if (localStorage.refresh_token) {
      $wnd.$.post('https://accounts.google.com/o/oauth2/token', {
        ...
   */
  private native void getTokenFromStorageImpl(String clientId, String clientSecret, Callback success, Callback failure) /*-{
    if (localStorage.refresh_token) {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('refreshing token');
      $wnd.$.post('https://accounts.google.com/o/oauth2/token', {
        refresh_token: localStorage.refresh_token,
        client_id: clientId,
        client_secret: clientSecret,
        grant_type: 'refresh_token'
      }).done(function(data) {
        success.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
      }).fail(function(response) {
        failure.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(response.responseJSON);
      });
    } else {
      failure.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)();
    }
  }-*/;
  
  private native void setTokenInStorageImpl(JavaScriptObject data) /*-{
    localStorage.access_token = data.access_token;
    localStorage.refresh_token = data.refresh_token || localStorage.refresh_token;
    var expiresAt = new Date().getTime() + parseInt(data.expires_in, 10) * 1000 - 60000;
    localStorage.expires_at = expiresAt;
    localStorage.token = data;
  }-*/;
  
  private native void setTokenInApiImpl (Token token) /*-{
    $wnd.gapi.auth.setToken(token);
  }-*/;

  private native void signInDesktopImpl (Boolean mode, String clientId, String clientSecret, String scopes, Callback success, Callback failure) /*-{
    $wnd.gapi.auth.authorize({
        client_id: clientId,
        scope: scopes,
        immediate: mode},
      function() {
        success.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)();
      });
  }-*/;

  private native void signInMobileImpl (Boolean mode, String clientId, String clientSecret, String scopes, Callback success, Callback failure) /*-{
    
    //Build the OAuth consent page URL
    var authUrl = 'https://accounts.google.com/o/oauth2/auth?' + $wnd.$.param({
      client_id: clientId,
      redirect_uri: 'http://localhost',
      response_type: 'code',
      scope: scopes
    });
    
    //Open the OAuth consent page in the InAppBrowser
    var authWindow = $wnd.open(authUrl, '_blank', 'location=no,toolbar=no');
    
    var loadstartHandler = function(e) {
        $wnd.glbDebugHook();
        var url = e.originalEvent.url;
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('received loadstart event with ' + url);
        
        var code = /\?code=(.+)$/.exec(url);
        var error = /\?error=(.+)$/.exec(url);
        
        if (code || error) {
            //Always close the browser when match is found
            authWindow.close();
        }
  
        if (code) {
            code = @it.mate.phgcommons.client.api.AbstractEndpointProxy::purgeBlanks(Ljava/lang/String;)(code[1]);
            @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("received auth code '" + code + "'");
            //Exchange the authorization code for an access token
            $wnd.$.post('https://accounts.google.com/o/oauth2/token', {
                code: code,
                client_id: clientId,
                client_secret: clientSecret,
                redirect_uri: 'http://localhost',
                grant_type: 'authorization_code'
            }).done(function(data) {
                success.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
            }).fail(function(response) {
                failure.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(response.responseJSON);
            });
        } else if (error) {
          failure.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(error[1]);
        }
        
    };
  
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('setting loadstart handler');
    $wnd.$(authWindow).on('loadstart', loadstartHandler);
    
  }-*/;
  
  private native void userAuthedLightImpl(Callback callback) /*-{
    callback.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)();
  }-*/;
  
  private native void userAuthedDeepImpl(Callback callback) /*-{
    var request = $wnd.gapi.client.oauth2.userinfo.get().execute(function(resp) {
      if (!resp.code) {
        callback.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)();
      } else {
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("received userinfo error code '" + resp.code + "'");
        var msg = @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)(resp);
        @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("full resp is '" + msg + "'");
      }
    });
  }-*/;

  public void auth() {
    if (signedIn) {
      signOut();
    } else {
      signIn(false);
    }
  }
  
  public void reAuthorize() {
    if (signedIn) {
      revokeTokenImpl(new Callback() {
        public void execute(JavaScriptObject jso) {
          signOut();
          signIn(false);
        }
      });
    } else {
      signIn(false);
    }
  }
  
  private void signOut() {
    PhgUtils.log("AbstractEndpointProxy: signing out...");
    signOutImpl();
    signedIn = false;
    if (authDelegate != null) {
      authDelegate.execute(signedIn);
    }
  }
  
  private native void revokeTokenImpl(Callback success) /*-{
    var token = localStorage.token;
    $wnd.$.post('https://accounts.google.com/o/oauth2/revoke', {
        token: token
    }).done(function(data) {
      success.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
    }).fail(function(resp) {
      var msg = @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)(resp);
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('revoking token failure - ' + msg);
      // chiamo ugualmente la success per andare avanti
      success.@it.mate.phgcommons.client.api.AbstractEndpointProxy.Callback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)();
    });
  }-*/;
  
  public static void resetAll() {
    signOutImpl();
  }
  
  private static native void signOutImpl() /*-{
    localStorage.access_token = null;
    localStorage.refresh_token = null;
    localStorage.expires_at = 0;
    localStorage.token = null;
    if (typeof $wnd.gapi != "undefined") {
      $wnd.gapi.auth.setToken(null);
      $wnd.gapi.auth.signOut();
    }
  }-*/;
  
  protected static interface Callback {
    public void execute(JavaScriptObject jso);
  }

  protected static class Token extends JavaScriptObject {
    protected Token() { }
    protected final String getAccessToken() {
      return (String)GwtUtils.getJsPropertyObject(this, "access_token");
    }
    protected final String getTokenType() {
      return (String)GwtUtils.getJsPropertyObject(this, "token_type");
    }
    protected final String getExpiresIn() {
      return (String)GwtUtils.getJsPropertyObject(this, "expires_in");
    }
    protected final String getIdToken() {
      return (String)GwtUtils.getJsPropertyObject(this, "id_token");
    }
    protected final String getRefreshToken() {
      return (String)GwtUtils.getJsPropertyObject(this, "refresh_token");
    }
    protected final String getError() {
      return (String)GwtUtils.getJsPropertyObject(this, "error");
    }
    protected final String getState() {
      return (String)GwtUtils.getJsPropertyObject(this, "state");
    }
    protected final String toMyString() {
      return "Token [getAccessToken()=" + getAccessToken() + ", getError()=" + getError() + ", getExpiresIn()=" + getExpiresIn() + ", getState()=" + getState()
          + "]";
    }
  }
  
  protected static String purgeBlanks(String code) {
    int pos;
    if ((pos = code.indexOf(' ')) > -1) {
      code = code.substring(pos);
    }
    PhgUtils.log("purged code = '" + code + "'");
    return code;
  }
  
}
