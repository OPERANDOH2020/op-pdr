package eu.operando.pdr.rpm.service;

import eu.operando.pdr.rpm.model.User;
import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
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
                
                List<Object> jpaSortedEntities = new ArrayList<>();
                
                
                
		/* Post Process Step */
                //if sortby Salary_ore desc
                ODataResponse oDataResponse = null;
                
                if(uriParserResultView.getStartEntitySet().getName().equals("Users") && uriParserResultView.getOrderBy()!=null && uriParserResultView.getOrderBy().getExpressionString().equals("Salary desc") ){
                    jpaSortedEntities = postProcess(jpaEntities);
                    
                    oDataResponse = responseBuilder.build(uriParserResultView, jpaSortedEntities, contentType);
                }else{
                     oDataResponse = responseBuilder.build(uriParserResultView, jpaEntities, contentType);
                }

                
                return oDataResponse;
	}

	private void preProcess() {

	}

	private List<Object> postProcess(List<Object> items){
            String[] values = new String[items.size()]; 
            List<Object> jpaSortedEntities = new ArrayList<>();
            
            for(int i=0; i<items.size();i++){
                User currentUser = (User) items.get(i);
                values[i] = currentUser.getSalary_ore();
            }
            
            String[] sortedValues = bubbleSort(values);
            
            for(int j=0; j<sortedValues.length;j++){
                for(int i=0; i<items.size();i++){
                    User currentUser = (User) items.get(i);
                    if(currentUser.getSalary_ore().equals(values[j])){
                        jpaSortedEntities.add(currentUser);
                        break;
                    }
                }
            }
            return jpaSortedEntities;
	}
	
        
        private String[] bubbleSort(String[] values)
        {
             int j;
             boolean flag = true;   // set flag to true to begin first pass
             String temp;   //holding variable

             while ( flag )
             {
                    flag= false;    //set flag to false awaiting a possible swap
                    for( j=0;  j < values.length -1;  j++ )
                    {
                           if(ore_compare(values[j].toCharArray(), values[j+1].toCharArray())==-1){
                               temp = values[ j ];                //swap elements
                               values[ j ] = values[ j+1 ];
                               values[ j+1 ] = temp;
                               flag = true;              //shows a swap occurred  
                           }
                    }
              }
             return values;
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