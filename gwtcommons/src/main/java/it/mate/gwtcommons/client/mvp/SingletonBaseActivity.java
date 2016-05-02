package it.mate.gwtcommons.client.mvp;

import it.mate.gwtcommons.client.factories.BaseClientFactory;

/**
 * 
 * WORK-AROUND PER IMPEDIRE AL ACTIVITYMANAGER DI CONSIDERARE IL SINGLETON LA STESSA ISTANZA
 *
 */

@SuppressWarnings("rawtypes")
public abstract class SingletonBaseActivity extends BaseActivity {

  public SingletonBaseActivity(BaseClientFactory clientFactory) {
    super(clientFactory);
  }
  
  @Override
  public boolean equals(Object obj) {
    return false;
  }

}
