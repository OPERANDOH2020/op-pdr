package eu.operando.pdr.rpm.service;

import java.util.List;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAProcessor;

public class CustomODataJPAProcessor extends ODataJPAProcessor{

	public CustomODataJPAProcessor(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
	}

	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriParserResultView, final String contentType)
			throws ODataException {

		/* Pre Process Step */
		preProcess();

		List<Object> jpaEntities = jpaProcessor.process(uriParserResultView);
				
		/* Post Process Step */
		postProcess(jpaEntities);

		ODataResponse oDataResponse =
				responseBuilder.build(uriParserResultView, jpaEntities, contentType);

		return oDataResponse;
	}

	private void preProcess() {

	}

	private void postProcess(List<Object> names){

	}
	
	void quickSort(String[] names, int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;
        String pivot = names[lowerIndex + (higherIndex - lowerIndex) / 2];

        while (i <= j) {
            while (ore_compare(names[i].toCharArray(), pivot.toCharArray()) < 0) {
                i++;
            }

            while (ore_compare(names[j].toCharArray(), pivot.toCharArray()) > 0) {
                j--;
            }

            if (i <= j) {
                exchangeNames(names, i, j);
                i++;
                j--;
            }
        }
        //call quickSort recursively
        if (lowerIndex < j) {
            quickSort(names, lowerIndex, j);
        }
        if (i < higherIndex) {
            quickSort(names, i, higherIndex);
        }
    }

    void exchangeNames(String[] names, int i, int j) {
        String temp = names[i];
        names[i] = names[j];
        names[j] = temp;
    }
    
    int ore_compare(char [] u, char [] v) {
    	if (u == v)
    		return 0;

    	int L = u.length;
    	int cnt = 0;
    	while (u[cnt] == v[cnt])
    		cnt += 1;
    	if ( ( (Character.getNumericValue(u[cnt]) + 1) % 3) ==  Character.getNumericValue(v[cnt])){
    		return -1;
    	}
    	else{
    		return 1;
    	}
    }

}