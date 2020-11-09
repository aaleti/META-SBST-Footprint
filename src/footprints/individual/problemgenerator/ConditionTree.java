
package footprints.individual.problemgenerator;

import java.io.Serializable;

/**
 *
 * @author aldeidaaleti
 */
public class ConditionTree implements Serializable{
		
        private final int treeDepth;
        private final int treeArity;

        private final int numberOfNodes;

        private ConditionStructure[] nodes;

        private StringBuilder conditionString;

        public ConditionTree(int treeDepth, int treeArity) {
                super();
                this.treeDepth = treeDepth;
                this.treeArity = treeArity;
                this.numberOfNodes = Double.valueOf(Math.pow(2, treeDepth) - 1).intValue();
                for(int i=0; i < numberOfNodes; i++){
                        ConditionStructure condition = new ConditionStructure(i, treeArity);
                        condition.newValues();
                        nodes[i]= condition;
                }
        }

        public void mutate(double propability){
                for(int i=0; i < numberOfNodes; i++){
                        if(Math.random() >= propability){
                              nodes[i].mutate();
                        }
                }
        }

        public String generate(){

                conditionString = new StringBuilder("(");

                for(int i=0; i < numberOfNodes; i++){
                        for(int x = 0; x < treeArity; x++){
                            conditionString.append(nodes[i].getConditionValueList()[x].getValueTypeList()[0].getDescription() + " number" + i + "" + x + ",");
                        }
                }

                conditionString.replace(conditionString.toString().length() - 1, conditionString.toString().length(), "");
                conditionString.append("){");

                String str = new String();
                for(int i=0, k=0; i < treeDepth; i++){
                        if(i == 0){
                                str = nodes[i].generate();
                                k++;
                        }else{
                                int numberOfNodes = Double.valueOf(Math.pow(2, i)).intValue();
                                if(i == treeDepth-1){
                                        for(int x=0, n=0; x < numberOfNodes; x++, k++){
                                                str = str.replaceFirst("#" + i +  "_" + x, nodes[k].generate("",""));
                                        }
                                }else{
                                        for(int x=0, n=0; x < numberOfNodes; x++, k++){
                                                str = str.replaceFirst("#" + i +  "_" + x, nodes[k].generate("#" + (i+1) + "_" + (n++),"#" + (i+1) + "_" + (n++)));
                                        }
                                }	
                        }
                }

                str = str.replaceAll("#1", "");

                conditionString.append(str);
                conditionString.append("}");

                return conditionString.toString();
        }

        public ConditionStructure[] getNodes() {
                return nodes;
        }

        public StringBuilder getConditionString() {
                return conditionString;
        }

        public void setConditionString(StringBuilder conditionString) {
                this.conditionString = conditionString;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((conditionString == null) ? 0 : conditionString.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                ConditionTree other = (ConditionTree) obj;
                if (conditionString == null) {
                        if (other.conditionString != null)
                                return false;
                } else if (!conditionString.toString().equals(other.conditionString.toString()))
                        return false;
                return true;
        }
        
        @Override
        public ConditionTree clone(){
            ConditionTree ct = new ConditionTree(treeDepth, treeArity);
            StringBuilder stb= new StringBuilder(conditionString);
            ct.conditionString = stb;
            ConditionStructure[] newnodes = new ConditionStructure[nodes.length];
            for(int i=0; i<nodes.length;i++){
                   ConditionStructure newcs=nodes[i].clone();
                   newnodes[i]= newcs;
            }
            ct.nodes=newnodes;
            return ct;       
        }

}	
	
