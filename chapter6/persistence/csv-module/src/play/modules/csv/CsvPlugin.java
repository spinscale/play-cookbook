package play.modules.csv;

import javax.persistence.Entity;

import play.Logger;
import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.db.Model;
import play.db.jpa.JPAPlugin.JPAModelLoader;

public class CsvPlugin extends PlayPlugin {

	private CsvEnhancer enhancer = new CsvEnhancer();
	
	@Override
    public void enhance(ApplicationClass applicationClass) throws Exception {
    	enhancer.enhanceThisClass(applicationClass);
    }
	
	@Override
	public void onApplicationStart() {
		CsvHelper.clean();
		Logger.info("Csv Plugin loaded");
	}

    @Override
    public Model.Factory modelFactory(Class<? extends Model> modelClass) {
		if (CsvModel.class.isAssignableFrom(modelClass)) {
			return new CsvModelFactory(modelClass);
		}
        return null;
    }
}
