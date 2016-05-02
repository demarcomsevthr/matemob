package it.mate.phgcommons.client.plugins;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgUtils;
import it.mate.phgcommons.client.utils.callbacks.JSOStringCallback;
import it.mate.phgcommons.client.utils.callbacks.JSOSuccess;

import com.google.gwt.core.client.JavaScriptObject;

public class FileSystemPlugin {
  
  
  /**
   * 
   *  DOCUMENTATION
   *  
   *    https://github.com/apache/cordova-plugin-file
   *    
   *    https://github.com/apache/cordova-plugin-file-transfer
   * 
   *    https://github.com/apache/cordova-plugin-file/blob/master/doc/index.md
   *  
   *    http://docs.phonegap.com/en/3.3.0/cordova_file_file.md.html
   *    
   *    http://www.html5rocks.com/en/tutorials/file/filesystem/
   *    
   * 
   * 
   *  INSTALLATION
   *    
   *    cordova plugin add cordova-plugin-file
   *    
   *    cordova plugin add cordova-plugin-file-transfer
   *  
   *  
   */
  
  
  /* 5 MB */
  private final static int DEFAULT_SIZE = 5 * 1024 * 1024;
  

  public static native boolean isInstalled () /*-{
    return typeof ($wnd.requestFileSystem) != 'undefined' && typeof ($wnd.resolveLocalFileSystemURL) != 'undefined';
  }-*/;
  
  

  /*
   * Esempio:
   *   sourceFile = "www/data/test.txt"
   *   destPath = "protoph/workArea"
   * 
   */
  public static void copyApplicationFileToTmpDir (final String sourceFile, final String destPath, final Delegate<String> delegate) {
    PhgUtils.log("COPYING " + sourceFile + " TO " + destPath);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("COPYING ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    getApplicationFileImpl(sourceFile, new JSOSuccess() {
      public void handle(final JavaScriptObject sourceFileEntry) {
        getTempDirImpl(OsDetectionUtils.isAndroid(),  new JSOSuccess() {
          public void handle(JavaScriptObject tempDirEntry) {
            createDirIfNotExistsImpl(tempDirEntry, destPath, new JSOSuccess() {
              public void handle(JavaScriptObject destDirEntry) {
                copyFileImpl(sourceFileEntry, destDirEntry, null, new JSOSuccess() {
                  public void handle(JavaScriptObject destFileEntry) {
                    PhgUtils.log("copied file entry " + JSONUtils.stringify(destFileEntry));
                    String result = GwtUtils.getJsPropertyString(destFileEntry, "fullPath");
                    delegate.execute(result);
                  }
                }, failure);
              }
            }, failure);
          }
        }, failure);
      }
    }, failure);
  }

  public static void deleteTmpDir (final String dirPath, final Delegate<String> delegate) {
    PhgUtils.log("DELETING " + dirPath);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("DELETING ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    getTempFileImpl(dirPath, OsDetectionUtils.isAndroid(), new JSOSuccess() {
      public void handle(final JavaScriptObject dirEntry) {
        deleteDirImpl(dirEntry, new JSOSuccess() {
          public void handle(JavaScriptObject deletedDirEntry) {
            PhgUtils.log("deleted dir entry " + JSONUtils.stringify(dirEntry));
            if (deletedDirEntry != null) {
              String result = GwtUtils.getJsPropertyString(deletedDirEntry, "fullPath");
              delegate.execute(result);
            } else {
              delegate.execute(null);
            }
          }
        }, failure);
      }
    }, failure);
  }
  
  public static void downloadRemoteFileToTempDir (final String sourceUrl, final String targetRelativePath, final Delegate<String> delegate) {
    PhgUtils.log("DOWNLOADING FROM " + sourceUrl + " TO " + targetRelativePath);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("DOWNLOADING ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    downloadRemoteFileToTempDirImpl(sourceUrl, targetRelativePath, OsDetectionUtils.isAndroid(), new JSOSuccess() {
      public void handle(JavaScriptObject targetFileEntry) {
        PhgUtils.log("target file = " + JSONUtils.stringify(targetFileEntry));
        String result = GwtUtils.getJsPropertyString(targetFileEntry, "nativeURL");
        delegate.execute(result);
      }
    }, failure);
  }
  
  public static void copyTempFileToTmpDir (final String sourceFile, final String destPath, final Delegate<String> delegate) {
    PhgUtils.log("COPYING " + sourceFile + " TO " + destPath);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("COPYING ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    getTempFileImpl(sourceFile, OsDetectionUtils.isAndroid(), new JSOSuccess() {
      public void handle(final JavaScriptObject sourceFileEntry) {
        getTempDirImpl(OsDetectionUtils.isAndroid(), new JSOSuccess() {
          public void handle(JavaScriptObject tempDirEntry) {
            createDirIfNotExistsImpl(tempDirEntry, destPath, new JSOSuccess() {
              public void handle(JavaScriptObject destDirEntry) {
                copyFileImpl(sourceFileEntry, destDirEntry, null, new JSOSuccess() {
                  public void handle(JavaScriptObject destFileEntry) {
                    PhgUtils.log("copied file entry " + JSONUtils.stringify(destFileEntry));
                    String result = GwtUtils.getJsPropertyString(destFileEntry, "fullPath");
                    delegate.execute(result);
                  }
                }, failure);
              }
            }, failure);
          }
        }, failure);
      }
    }, failure);
  }

  

  /*****************************************************************************************************************/
  
  private static native void requestPersistentFileSystemImpl (int size, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileSystem) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileSystem);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    $wnd.requestFileSystem($wnd.PERSISTENT, size, jsSuccess, jsFailure);    
  }-*/;

  private static native void requestTemporaryFileSystemImpl (int size, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileSystem) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileSystem);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    $wnd.requestFileSystem($wnd.TEMPORARY, size, jsSuccess, jsFailure);    
  }-*/;

  private static native void getRootFileImpl (String src, JavaScriptObject fs, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    fs.root.getFile(src, {}, jsSuccess, jsFailure);
  }-*/;

  
  private static void getApplicationFileImpl (String src, JSOSuccess success, JSOStringCallback failure) {
    if (OsDetectionUtils.isAndroid()) {
      String fileName = src.substring(src.lastIndexOf("/") + 1);
      getApplicationFileAndroidImpl(src, fileName, success, failure);
    } else {
      getApplicationFileDefaultImpl(src, success, failure);
    }
  }
  
  private static native void getApplicationFileDefaultImpl (String src, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    $wnd.resolveLocalFileSystemURL($wnd.cordova.file.applicationDirectory + src, jsSuccess, jsFailure);
  }-*/;

  private static native void getApplicationFileAndroidImpl (String srcPath, String fileName, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    var fileApplUrl = $wnd.cordova.file.applicationDirectory + srcPath;
    var fileTransfer = new $wnd.FileTransfer();
    var fileDataPath = $wnd.cordova.file.dataDirectory + fileName;
    fileTransfer.download($wnd.encodeURI(fileApplUrl), fileDataPath, (jsSuccess), (jsFailure), true);
  }-*/;

  private static native void getTempFileImpl (String src, boolean isAndroid, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    var tempDir = isAndroid ? $wnd.cordova.file.dataDirectory : $wnd.cordova.file.tempDirectory;
    $wnd.resolveLocalFileSystemURL(tempDir + src, jsSuccess, jsFailure);
  }-*/;

  private static native void getTempDirImpl (boolean isAndroid, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    var tempDir = isAndroid ? $wnd.cordova.file.dataDirectory : $wnd.cordova.file.tempDirectory;
    $wnd.resolveLocalFileSystemURL(tempDir, jsSuccess, jsFailure);
  }-*/;
  

  private static native void createDirIfNotExistsImpl (JavaScriptObject rootDirEntry, String path, JSOSuccess success, JSOStringCallback failure) /*-{
    @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("createDirIfNotExistsImpl.1 " + path);
    
    var jsSuccess = $entry(function(fileEntry) {
      var str = @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("createDirIfNotExistsImpl.success " + str);
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      var str = @it.mate.phgcommons.client.utils.JSONUtils::stringify(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("createDirIfNotExistsImpl.failure " + str);
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    var jsCreateDirFn = $entry(function (rootDirEntry, folders) {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)("createDirIfNotExistsImpl.2 " + folders[0]);
      if (folders[0] == '.' || folders[0] == '') {
        folders = folders.slice(1);
      }
      rootDirEntry.getDirectory(folders[0], {create: true}, function(dirEntry) {
        // Recursively add the new subfolder (if we still have another to create).
        if (folders.length > 1) {
          jsCreateDirFn(dirEntry, folders.slice(1));
        } else {
          jsSuccess(dirEntry);
        }
      }, jsFailure);
    });
    jsCreateDirFn(rootDirEntry, path.split('/'));
  }-*/;

  private static native void copyFileImpl (JavaScriptObject fileEntry, JavaScriptObject destDirEntry, String newName, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    if (newName == null) {
      newName = fileEntry.name;
    }
    fileEntry.copyTo(destDirEntry, newName, jsSuccess, jsFailure);
  }-*/;
  
  private static native void deleteDirImpl (JavaScriptObject dirEntry, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    dirEntry.removeRecursively(jsSuccess, jsFailure);
  }-*/;
  
  /**
   * Example:
   * 
   * sourceUrl = "http://some.server.com/download.php"
   * targetRelativePath = "protoph/downloadArea"
   * 
   * see:
   * 
   *   https://github.com/apache/cordova-plugin-file-transfer/blob/master/doc/index.md
   * 
   */
  
  private static native void downloadRemoteFileToTempDirImpl (String sourceUrl, String targetRelativePath, boolean isAndroid, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(fileEntry) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(fileEntry);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    
    var fileTransfer = new $wnd.FileTransfer();
    var uri = $wnd.encodeURI(sourceUrl);
    var tempDir = isAndroid ? $wnd.cordova.file.dataDirectory : $wnd.cordova.file.tempDirectory;
    fileTransfer.download(uri, tempDir + targetRelativePath, jsSuccess, jsFailure, true );    
    
  }-*/;

  

  public static void testPlugin (final String sourceFile, final String destPath, final Delegate<String> delegate) {
    PhgUtils.log("PLUGIN TEST source = " + sourceFile + " dest = " + destPath);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("PLUGIN TEST ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    
    getApplicationFileImpl(sourceFile, new JSOSuccess() {
      public void handle(final JavaScriptObject sourceFileEntry) {
        PhgUtils.log("source file = " + JSONUtils.stringify(sourceFileEntry));
        getTempDirImpl(OsDetectionUtils.isAndroid(), new JSOSuccess() {
          public void handle(JavaScriptObject tempDirEntry) {
            PhgUtils.log("temp dir = " + JSONUtils.stringify(tempDirEntry));
            createDirIfNotExistsImpl(tempDirEntry, destPath, new JSOSuccess() {
              public void handle(JavaScriptObject destDirEntry) {
                PhgUtils.log("created dest dir = " + JSONUtils.stringify(destDirEntry));
              }
            }, failure);
          }
        }, failure);
        
      }
    }, failure);
  }
  
  public static void readApplicationFileAsEncodedData (final String sourceFile, final Delegate<String> delegate) {
    PhgUtils.log("READING " + sourceFile);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("COPYING ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    getApplicationFileImpl(sourceFile, new JSOSuccess() {
      public void handle(JavaScriptObject sourceFileEntry) {
        
        PhgUtils.log("getting file from file entry " + sourceFileEntry);
        
        getFileFromFileEntryImpl(sourceFileEntry, new JSOSuccess() {
          public void handle(JavaScriptObject file) {
            
            PhgUtils.log("reading file " + file);
            
            readFileAsEncodedDataImpl(file, new JSOSuccess() {
              public void handle(JavaScriptObject evt) {
                JavaScriptObject target = GwtUtils.getJsPropertyJso(evt, "target");
                String result = GwtUtils.getJsPropertyString(target, "result");
                delegate.execute(result);
              }
            }, failure);
            
            
          }
        }, failure);
        
      }
    }, failure);
  }
  
  private static native void getFileFromFileEntryImpl (JavaScriptObject fileEntry, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(file) {
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(file);
    });
    var jsFailure = $entry(function(error) {
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    fileEntry.file(jsSuccess, jsFailure);
  }-*/;
  
  private static native void readFileAsEncodedDataImpl (JavaScriptObject fileEntryFile, JSOSuccess success, JSOStringCallback failure) /*-{
    var jsSuccess = $entry(function(evt) {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('READ SUCCESS!');
      success.@it.mate.phgcommons.client.utils.callbacks.JSOSuccess::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(evt);
    });
    var jsFailure = $entry(function(error) {
      @it.mate.phgcommons.client.utils.PhgUtils::log(Ljava/lang/String;)('READ FAILURE!');
      failure.@it.mate.phgcommons.client.utils.callbacks.JSOStringCallback::handle(Ljava/lang/String;)(error.code);
    });
    
    var reader = new $wnd.FileReader();
    reader.onload = (jsSuccess);
    reader.onerror = (jsFailure);
    reader.readAsDataURL(fileEntryFile);
    
  }-*/;
  
  public static void readExternalFileAsEncodedData (final String sourceUrl, String targetRelativePath, final Delegate<String> delegate) {
    final String fTargetRelativePath = targetRelativePath != null ? targetRelativePath : "file.tmp";    
    PhgUtils.log("DOWNLOADING FROM " + sourceUrl + " TO " + fTargetRelativePath);
    final JSOStringCallback failure = new JSOStringCallback() {
      public void handle(String errorCode) {
        PhgUtils.log("DOWNLOADING ERROR CODE = " + errorCode);
        delegate.execute(null);
      }
    };
    downloadRemoteFileToTempDirImpl(sourceUrl, fTargetRelativePath, OsDetectionUtils.isAndroid(), new JSOSuccess() {
      public void handle(JavaScriptObject targetFileEntry) {
        PhgUtils.log("downloaded file = " + JSONUtils.stringify(targetFileEntry));
        String fullPath = GwtUtils.getJsPropertyString(targetFileEntry, "fullPath");
        double size = GwtUtils.getJsPropertyDouble(targetFileEntry, "size");
        PhgUtils.log("fullPath = " + fullPath);
        PhgUtils.log("size = " + size);
        
        getFileFromFileEntryImpl(targetFileEntry, new JSOSuccess() {
          public void handle(JavaScriptObject targetFile) {
            PhgUtils.log("reading file " + targetFile);
            readFileAsEncodedDataImpl(targetFile, new JSOSuccess() {
              public void handle(JavaScriptObject evt) {
                JavaScriptObject target = GwtUtils.getJsPropertyJso(evt, "target");
                String result = GwtUtils.getJsPropertyString(target, "result");
                delegate.execute(result);
              }
            }, failure);
          }
        }, failure);
        
      }
    }, failure);
  }
  
}
