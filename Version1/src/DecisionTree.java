import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Collections;

public class DecisionTree{
    ArrayList<DataEntry> parent_data_set=new ArrayList<>();
    ArrayList<DataEntry> data_set=new ArrayList<>();
    ArrayList<String> class_values=new ArrayList<>();
    LinkedHashSet<Atribute> atributes=new LinkedHashSet<>();
    HashMap<String,DecisionTree> descendents=new HashMap<>();
    Atribute split=null;
    String classification="";

    public DecisionTree(ArrayList<DataEntry> data,ArrayList<DataEntry> parent_data,
        ArrayList<String>class_values,LinkedHashSet<Atribute>atributes){
        this.data_set=data;
        this.parent_data_set=parent_data;
        this.class_values=class_values;
        this.atributes=atributes;
    }

    public boolean isPure(ArrayList<DataEntry> data_set){
        String s=null;
        for(DataEntry data: data_set){
            if(s==null)
                s=data.getClassification();
            else{
                if(!data.getClassification().equals(s))
                    return false;
            }
        }
        return true;
    }


    public double individualEntropy(double p){
        return ((p==0 || p==1) ? 0 : (-(p*(Math.log(p))/*/Math.log(2))));*/ + (1-p)*(Math.log(1-p)))) );
    }

    public Atribute getSplit(){return split;}
    public Atribute splitOnAtribute(LinkedHashSet<Atribute> atributes,ArrayList<DataEntry> data_set, ArrayList<String> class_val){
        double min_entropy=Double.MAX_VALUE;
        Atribute split_on=null;

        for(Atribute atribute : atributes){
            double entropy=0;
            int n=data_set.size();

            for(String atribute_value : atribute.getAtributeVals()){
                int p=0;
                int count=0;
                for(DataEntry data : data_set){
                if(atribute.isContinuos()){
                        if(Double.parseDouble(data.getAtributeVal(atribute.getAtribute())) <= Double.parseDouble(atribute_value)){
                            count++;
                            if(data.getClassification().equals(class_val.get(0)))
                                p++;
                        }
                    }
                    else{
                        if(data.getAtributeVal(atribute.getAtribute()).equals(atribute_value)){
                            count++;
                            if(data.getClassification().equals(class_val.get(0)))
                                p++;
                        }
                    }
                }
                if(count !=0)
                    entropy+=((double)count)/ n *individualEntropy((double)p/count);
            }
            if(entropy<min_entropy){
                min_entropy=entropy;
                split_on=atribute;
            }
        }
        /*for(Atribute atribute : atributes){
            double entropy=0;
            int n=data_set.size();
            for(String atribute_value : atribute.getAtributeVals()){
                HashMap<String,Integer> times_for_class=new HashMap<>();
                for(String cls : class_values)
                    times_for_class.put(cls,0);
                int count=0;
                for(DataEntry data : data_set){
                    if(data.getAtributeVal(atribute.getAtribute()).equals(atribute_value)){
                        count++;
                        for(String c : times_for_class.keySet()){
                            if(data.getClassification().equals(c)){
                                times_for_class.put(c,times_for_class.get(c)+1);
                                break;
                            }
                        }
                    }
                }
                if(count!=0){
                    double sum=0;
                    for(String c : times_for_class.keySet()){
                        double p=((double)times_for_class.get(c))/(double)count;
                        sum+=(-1)*p*(Math.log(p)/Math.log(2));
                    }
                    entropy+=(((double)count)/(double)n)*sum;
                }
            }
            if(entropy<min_entropy){
                min_entropy=entropy;
                split_on=atribute;
            }
        }*/
        return split_on;
    }

    public String mostCommonTarget(ArrayList<DataEntry> data_set){
        HashMap<String,Integer> corresondance=new HashMap<>();
        for(DataEntry data : data_set){
            if(!corresondance.containsKey(data.getClassification())){
                corresondance.put(data.getClassification(),0);
            }
            corresondance.put(data.getClassification(),corresondance.get(data.getClassification())+1);
        }

        return Collections.max(corresondance.entrySet(),Map.Entry.comparingByValue()).getKey();
    }

    public String classify(DataEntry data){
        if(classification=="")
            return descendents.get(data.getAtributeVal(split.getAtribute())).classify(data);
        return classification;
    }

    public void buildTree(){
        if(data_set.isEmpty())
            this.classification=mostCommonTarget(parent_data_set);
        else if(isPure(data_set)){
            for(DataEntry data : data_set){
                classification=data.getClassification();
                break;
            }
        }
        else if(atributes.isEmpty())
            this.classification=mostCommonTarget(data_set);
        else{
            split=splitOnAtribute(atributes, data_set, class_values);
            
            for(String possible_value : split.getAtributeVals()){
                ArrayList<DataEntry> sub_data_set=new ArrayList<>();
                for(DataEntry data : data_set){
                    /*if(split.isContinuos()){
                        if(Double.parseDouble(data.getAtributeVal(split.getAtribute())) <= Double.parseDouble(possible_value)){
                            sub_data_set.add(data);
                        }
                    }
                    else{*/
                        if(data.getAtributeVal(split.getAtribute()).equals(possible_value))
                            sub_data_set.add(data);
                    //}
                }
                LinkedHashSet<Atribute> sub_atributes=new LinkedHashSet<Atribute>(atributes);
                sub_atributes.remove(split);

                DecisionTree sub_tree=new DecisionTree(sub_data_set, data_set, class_values, sub_atributes);
                sub_tree.buildTree();
                descendents.put(possible_value,sub_tree);
            }
        }
    }

    public void prunTree(DecisionTree tree){
        if(tree.getSplit().isContinuos()){
            if(tree.getSplit()!=null){
                
                for(DecisionTree node : tree.descendents.values()){
                    
                }
            }
            else
                return;
        }
        else return;
    }
    public void printTree(){
        printTree("");
    }
    private void printTree(String prefix){
        if (split != null) {  // Still traveling down tree
			System.out.printf ("%s <%s>\n", prefix, split.getAtribute());
			System.out.printf ("%s    | \\\n", prefix);
			
			// Iterate over values
			Iterator <String> it = descendents.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
                if (it.hasNext()) {  // more to come
                    /*if(split.isContinuos()){
                        System.out.printf("%s    |  <=%s:\n", prefix, key);
                    }
                    else{*/
                        System.out.printf("%s    |  = %s:\n", prefix, key);
                //}
					descendents.get(key).printTree(prefix + "    | ");
				}
				else {  // last one
					System.out.printf("%s     \\\n", prefix);
					System.out.printf("%s       = %s:\n", prefix, key);
					descendents.get(key).printTree(prefix + "      ");
				}
			}
        } else {  // leaf node
            System.out.printf("%s  [%s]\n", prefix, classification);
		}
    }
}