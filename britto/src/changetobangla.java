
/* ------------------
 * Developed by:
 * Md. Tahseen Anam
--------------------- */

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sqlite.*;

import simplenlg.features.Feature;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class changetobangla {
    
	static Statement statement=null;
	static Connection c=null;
	static String person = "";
	//static ResultSet rs=null;
	static String aux=" ";
	static boolean printedpreposition = false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 changetobangla fileSearch = new changetobangla();
         
		 Statement statement=null;
		 String bangla=" ";
		 try {
		 c=DriverManager.getConnection("jdbc:sqlite:translateenbn.db");
		 System.out.println("Database opened successfully");
		 Font customFont = Font.createFont(Font.TRUETYPE_FONT, new
		 File("SolaimanLipi_22-02-2012.ttf")).deriveFont(12f);
//		 javafx.scene.text.Font custFont = javafx.scene.text.Font.loadFont(changetobangla.class.getResourceAsStream("/fonts/SolaimanLipi_22-02-2012.ttf"), 12);
//		 Font customFont = Font.createFont(Font.TRUETYPE_FONT, new
//				 File("BHRAC_.TTF")).deriveFont(12f);
		 GraphicsEnvironment ge =
		 GraphicsEnvironment.getLocalGraphicsEnvironment();
		 //register the font
		 ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new
		 File("SolaimanLipi_22-02-2012.ttf")));
//		 ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new
//				 File("BHRAC_.TTF")));
		 //use the font
		 c.close();
		
		 } catch (SQLException | FontFormatException | IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
	
		Scanner sc = new Scanner(new BufferedInputStream(System.in));
		String line = sc.nextLine();
		String[] words = line.split("\\s+");
		String[] subject = new String[words.length];
		String[] question = new String[words.length];
		String[] object = new String[words.length];
		String[] preposition = new String[words.length];
		String[] verb = new String[words.length];
		String[] verbform = new String[words.length];
		String[] auxiliary = new String[words.length];
		String[] regrp = new String[words.length];
		
		int i, k,j;
		String st = " ";
		String tempquestion="";
		k = 0;
		boolean foundquestion = false;
		/*To find if the sentence is a question*/
		Scanner sccques;
		
		try {
			sccques = new Scanner(new File("src/question.txt"));
			while(sccques.hasNext()){
	            String lnv = sccques.nextLine().toLowerCase().toString();
	            if(lnv.matches(".*\\b" + words[0].toLowerCase() + "\\b.*")) {
	            	
	            		foundquestion = true;
	            		tempquestion = words[0].toLowerCase();
	            		System.out.println("found question word...");
		            	break;
	            
	            }
	        }
			sccques.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (i = 0; i < words.length; i++) {
            System.out.println("chechking subject...");
			if(foundquestion) {
				
				if(isContain(words[i].toLowerCase(), "i") || isContain(words[i].toLowerCase(), "we")){
					
					if(words[0].toLowerCase().equals("have")||words[0].toLowerCase().equals("has")||words[0].toLowerCase().equals("had")||words[0].toLowerCase().equals("am")||words[0].toLowerCase().equals("is")||words[0].toLowerCase().equals("are")||words[0].toLowerCase().equals("was")||words[0].toLowerCase().equals("were")||words[0].toLowerCase().equals("will")||words[0].toLowerCase().equals("shall")) {
	            		question[0]="what";
	            	
	            	}else {
	            	question[0]=words[0].toLowerCase();
         	
	            	}
					
					subject[0]=words[i].toLowerCase();
					//System.out.println("Subject(First person) : " + subject[k]);
					String temp = words[i];
					for(int m=i;m>0;m--) {
						
						
						words[m]=words[m-1];
					}
					words[0]=temp;
					break;
				}else if(isContain(words[i].toLowerCase(), "you")){
					
					if(words[0].toLowerCase().equals("have")||words[0].toLowerCase().equals("has")||words[0].toLowerCase().equals("had")||words[0].toLowerCase().equals("am")||words[0].toLowerCase().equals("is")||words[0].toLowerCase().equals("are")||words[0].toLowerCase().equals("was")||words[0].toLowerCase().equals("were")||words[0].toLowerCase().equals("will")||words[0].toLowerCase().equals("shall")) {
	            		question[0]="what";
	            	
	            	}else {
	            	question[0]=words[0].toLowerCase();
         	
	            	}
										
					subject[0]=words[i].toLowerCase();
					person="secondperson";
					System.out.println("Subject(second person) : " + subject[k]);
					String temp = words[i];
					for(int m=i;m>0;m--) {
						
						
						words[m]=words[m-1];
					}
					words[0]=temp;
					break;
				}else if(words[i].toLowerCase().equals(tempquestion)){
					
					System.out.println("Ooops!this is not a subject,detected it as question...");
					
				}else {
					
					Scanner sccc;
					boolean verbinqueue = false;
					boolean preinqueue = false;
					try {
						sccc = new Scanner(new File("src/verb.txt"));
						while(sccc.hasNext()){
				            String lnv = sccc.nextLine().toLowerCase().toString();
				            if(lnv.matches(".*\\b" + words[i].toLowerCase() + "\\b.*")) {
				            	
				            	    verbinqueue = true;
					            	break;
				            
				            }
				        }
						sccc.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(verbinqueue==false) {
						
						Scanner sccpre;
						
						try {
							sccpre = new Scanner(new File("src/prepositions.txt"));
							while(sccpre.hasNext()){
					            String lnv = sccpre.nextLine().toLowerCase().toString();
					            if(lnv.matches(".*\\b" + words[i].toLowerCase() + "\\b.*")) {
					            	
					            	    preinqueue = true;
						            	break;
					            
					            }
					        }
							sccpre.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
                  
				if(verbinqueue==false&&preinqueue==false) {	
					if(words[0].toLowerCase().equals("have")||words[0].toLowerCase().equals("has")||words[0].toLowerCase().equals("had")||words[0].toLowerCase().equals("am")||words[0].toLowerCase().equals("is")||words[0].toLowerCase().equals("are")||words[0].toLowerCase().equals("was")||words[0].toLowerCase().equals("were")||words[0].toLowerCase().equals("will")||words[0].toLowerCase().equals("shall")) {
	            		question[0]="what";
	            	
	            	}else {
	            	question[0]=words[0].toLowerCase();
         	
	            	}
					subject[0]=words[i].toLowerCase();
					person="thirdperson";
					//System.out.println("Subject(third person) : " + subject[k]);
					String temp = words[i];
					for(int m=i;m>0;m--) {
						
						
						words[m]=words[m-1];
					}
					words[0]=temp;
					break;
				}
				}
			}else {
			
			if(isContain(words[0].toLowerCase(), "i") || isContain(words[0].toLowerCase(), "we")){
				
				subject[0]=words[0].toLowerCase();
				//System.out.println("Subject(First person) : " + subject[k]);
				break;
			}else if(isContain(words[0].toLowerCase(), "you")){
				subject[0]=words[0].toLowerCase();
				person="secondperson";
				//System.out.println("Subject(second person) : " + subject[k]);
				break;
			}else {
				
				Scanner sccc;
				boolean verbinqueue = false;
				boolean preinqueue = false;
				try {
					sccc = new Scanner(new File("src/verb.txt"));
					while(sccc.hasNext()){
			            String lnv = sccc.nextLine().toLowerCase().toString();
			            if(lnv.matches(".*\\b" + words[i].toLowerCase() + "\\b.*")) {
			            	
			            	    verbinqueue = true;
				            	break;
			            
			            }
			        }
					sccc.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(verbinqueue==false) {
					
					Scanner sccpre;
					
					try {
						sccpre = new Scanner(new File("src/prepositions.txt"));
						while(sccpre.hasNext()){
				            String lnv = sccpre.nextLine().toLowerCase().toString();
				            if(lnv.matches(".*\\b" + words[i].toLowerCase() + "\\b.*")) {
				            	
				            	    preinqueue = true;
					            	break;
				            
				            }
				        }
						sccpre.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
				if(verbinqueue==false&&preinqueue==false) {	
				subject[0]=words[0].toLowerCase();
				person="thirdperson";
				//System.out.println("Subject(third person) : " + subject[k]);
				break;
				}
			}
			}
		}
		j = 0;
		for (i = 1; i < words.length; i++) {
            
		
	       
			//System.out.println("Checking for object...");
			if (isContain(words[i], "you") || isContain(words[i], "me") || isContain(words[i], "him")
					|| isContain(words[i], "us") || isContain(words[i], "them") || isContain(words[i], "his")
					|| isContain(words[i], "her") || isContain(words[i], "it") || isContain(words[i], "them")
					|| isContain(words[i], "this")) {
				object[j] = words[i];
				System.out.println("Objects found : " + object[j]);
				j++;
			}else {
				
				Scanner scc;
				boolean foundverb = false;
				//System.out.println("It is not an object.Starting to check if it is a preposition...");
				try {
											
					scc = new Scanner(new File("src/prepositions.txt"));
				    while(scc.hasNext()){
				            String ln = scc.nextLine().toLowerCase().toString();
				            if(ln.matches(".*\\b" + words[i] + "\\b.*")) {
				            	//System.out.println("'"+words[i]+"' is a preposition.");
				            	//System.out.println("Starting to check if it is a preposition before a verb...");
				            	scc = new Scanner(new File("src/verb.txt"));
							    while(scc.hasNext()){
							            String lnv = scc.nextLine().toLowerCase().toString();
							            if(lnv.matches(".*\\b" + words[i+1] + "\\b.*")) {
							            	
							            	foundverb = true;
							            	//System.out.println("Yes, it is a preposition before a verb. Ignoring this preposition as it will be rechecked when we start checking verb.");
							            }
							        }
								if(foundverb==false) {
								//System.out.println("'"+words[i]+"' is not a preposition before a verb. So,storing the preposition...");	
				            	object[j] = words[i];
				            	System.out.println(object[j]);
				            	j++;
								}
				            }
				        }
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		k = 0;
		for (i = 0; i < words.length; i++) {

			if (isContain(words[i], "am") || isContain(words[i], "is") || isContain(words[i], "was")
					|| isContain(words[i], "will") || isContain(words[i], "shall") || isContain(words[i], "are")
					|| isContain(words[i], "were") || isContain(words[i], "are") || isContain(words[i], "have") || isContain(words[i], "has") || isContain(words[i], "had")) {

				auxiliary[k] = words[i];
				//System.out.println("Auxiliary : " + auxiliary[k]);
				k++;
			}
		}
		k = 0;
		for (i = 0; i < words.length; i++) {

			//System.out.println("word:" + words[i]);
			//System.out.println();
			if (check(subject, words[i])) {

				//System.out.println("Nope!! subject found, continue..");
				//System.out.println();

			} else if (check(object, words[i])) {

				//System.out.println("Nope!! object found, continue..");
				//System.out.println();

			} else if (check(auxiliary, words[i])) {

				//System.out.println("Nope!! auxiliary found, continue..");
				//System.out.println();
			}else if(check(question, words[i])){
				
		      
			} else {
				
		        try {
		        
					
					//verb[k]=getverbwithform[1];
					Scanner scc;
					boolean prepos = false;
					if(words[i].toLowerCase().equals("to")) {
						prepos = true;
						//System.out.println("found preposition TO before verb: "+words[i+1]);
					}else {
						try {
							scc = new Scanner(new File("src/prepositions.txt"));
						    while(scc.hasNext()){
						            String ln = scc.nextLine().toLowerCase().toString();
						            if(ln.matches(".*\\b" + words[i].toLowerCase() + "\\b.*")) {
						            	
						            	verb[k] = words[i].toLowerCase();
						            	k++;
						            	prepos = true;
						            }
						        }
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
					if(prepos==false) {
						
					String returnedverb=fileSearch.parseFile("src/verb.txt", words[i]);
					System.out.println(returnedverb+" Auxi is: "+words[i-1]);
					String getverbwithform[] = returnedverb.split(",");	
						
						
					if(words[i-1].toLowerCase().equals("to")) {
		        		verb[k] = "infinite,"+getverbwithform[1];
		        	}else if(words[i-1].toLowerCase().equals("been")) {
		        		verb[k] = "pastcontinuous,"+getverbwithform[1];
		        	}else if(words[i-1].toLowerCase().equals("will")||words[i-1].toLowerCase().equals("shall")) {
		        		verb[k] = "future,"+getverbwithform[1];
		        	}else if(words[i-1].toLowerCase().equals("have")||words[i-1].toLowerCase().equals("has")) {
		        		verb[k] = "presentperfect,"+getverbwithform[1];
		        	}else if(words[i-1].toLowerCase().equals("had")) {
		        		verb[k] = "pastperfect,"+getverbwithform[1];
		        	}else {
					    verb[k]=returnedverb;
		        	}
					k++;
					}
					
					//System.out.println(verb[k]);
		        } catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//verb[k] = words[i];
				//System.out.println(verb[k]);
				//System.out.println();
				//k++;
			}

		}
        
		for (i = 0; i < words.length; i++) {

			//System.out.println("Checking for words other than subject,object,verbs...");
			//System.out.println();
			if (check(subject, words[i])) {

				//System.out.println("Nope!! subject found, continue..");
				//System.out.println();

			} else if (check(object, words[i])) {

				//System.out.println("Nope!! object found, continue..");
				//System.out.println();

			} else if (check(auxiliary, words[i])) {

				//System.out.println("Nope!! auxiliary found, continue..");
				//System.out.println();
			} else if(check(verb, words[i])){
				
		      
			}else if(check(question, words[i])){
				
		      
			}else {
				boolean prepos = false;
				try {
				Scanner scc = new Scanner(new File("src/prepositions.txt"));
				    while(scc.hasNext()){
				            String ln = scc.nextLine().toLowerCase().toString();
				            if(ln.matches(".*\\b" + words[i] + "\\b.*")) {
                                 prepos = true;
				            }
				        }
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(prepos==false) {
				object[j] = words[i];
				j++;
				}
			}

		}
		
		
		//j=0;
		print(subject,"SUBJECT");
		print(question,"QUESTION");
		print(object,"OBJECT");
		print(auxiliary,"AUXILIARY");
		print(verb,"VERB");
		

	}
   
    public String parseFile(String fileName,String searchStr) throws FileNotFoundException{
        Scanner scan = new Scanner(new File(fileName));
        String tense = "";
        while(scan.hasNext()){
            String line = scan.nextLine().toLowerCase().toString();
            if(line.matches(".*\\b" + searchStr + "\\b.*")) {//contains(searchStr)){
            	String[] verbinline = line.split(",");
            	//System.out.println("Word to return: "+verbinline[0]);
                Lexicon lexicon=Lexicon.getDefaultLexicon();
                NLGFactory nlgFactory=new NLGFactory(lexicon);
                Realiser realiser=new Realiser(lexicon);
                SPhraseSpec p=nlgFactory.createClause();
            	p.setVerb(verbinline[0]);
            	p.setFeature(Feature.TENSE, Tense.PAST); 
            	String outputpast = realiser.realiseSentence(p);
            	p.setFeature(Feature.TENSE, Tense.PRESENT);
            	p.setFeature(Feature.PERSON, Person.FIRST);
            	String outputpresent = realiser.realiseSentence(p);
            	//System.out.println("Past form: "+outputpast.replaceAll("[-+.^:,]","").toLowerCase());
            	//System.out.println("Present form: "+outputpresent.replaceAll("[-+.^:,]","").toLowerCase());
            	//System.out.println("Present form: "+verbinline[0]);.replaceFirst(".$","")
            	if(searchStr.contains("ing")) {
                	tense="Continuous";
                }else if(searchStr.matches("\\b"+outputpresent.replaceAll("[-+.^:,]","").toLowerCase()+"\\b")){
            	//}else if(searchStr.matches("\\b"+verbinline[0]+"\\b")){.replaceFirst(".$","")
                	tense="Present";
                }else if(searchStr.contains(outputpast.replaceAll("[-+.^:,]","").toLowerCase())) {
                	tense="Past";
                }else {
                	
                	tense="Perfect";
                }
                System.out.println("Word to return: "+outputpresent.replaceAll("[-+.^:,]","").toLowerCase());
            	return tense+","+outputpresent.replaceAll("[-+.^:,]","").toLowerCase();
                //return tense+","+verbinline[0];.replaceFirst(".$","")
                //System.out.println(line);
     
            }
        }
		return "";
    }
	
	private static boolean isContain(String source, String subItem) {
		source = source.toLowerCase();
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}

	public static boolean check(String[] check, String words) {

		int k = 0;
		try {
			for (k = 0; k < check.length; k++) {

				//System.out.println("Checking " + words + " With " + check[k]);
				//System.out.println();
				if (check[k] != null && words.toLowerCase().equals(check[k].toLowerCase())) {

					//System.out.println("Match found for :: " + words + " : " + check[k]);
					return true;

				}

			}
			return false;
		} catch (Exception ex) {

			System.out.println(ex);
			return false;
		}

	}

	public static void print(String[] check, String table) {

		int k = 0;
		boolean preposition=false;
		//System.out.println(table+","+k);
		check = Arrays.stream(check)
                 .filter(s -> (s != null && s.length() > 0))
                 .toArray(String[]::new);
		k = check.length-1;
		
		try {
			
			if(table=="OBJECT") {
				
				for (k = 0; k < check.length; k++) {
                        //System.out.println(check.length+","+k);
						if (check[k] != null) {
		                    
							Scanner scc;
							try {
								scc = new Scanner(new File("src/prepositions.txt"));
							    while(scc.hasNext()){
							            String ln = scc.nextLine().toLowerCase().toString();
							            if(ln.matches(".*\\b" + check[k] + "\\b.*")) {
							            	
							            	preposition=true;
							            }
							        }
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                           
							if(preposition) {
								printedpreposition = true;
								String returnedbangla = gettranslated(check[k+1],table);
								/*if(returnedbangla.charAt(returnedbangla.length()-1)=='\u0995') {
									returnedbangla.replaceFirst(".$","à¦°");
								}else if(returnedbangla.charAt(returnedbangla.length()-1)=='à¦°'){
									returnedbangla.replaceFirst(".$","à¦°à§‡à¦°");
								}*/
								System.out.print(returnedbangla+" ");
								System.out.print(gettranslated(check[k].toLowerCase(),"PREPOSITION")+" ");
								preposition=false; 
								//printedpreposition = false;
								k=k+1;
							}else { 
							System.out.print(gettranslated(check[k],table)+" ");
							}
						}

					}
			}else {
				if(table=="VERB") {
					//System.out.println(check.length-1+" "+k);
					for (k = k; k>=0; k--) {
					
                            if (check[k] != null) {
		                    //System.out.println(check[k]+":"+k);
							Scanner scc;
							try {
								scc = new Scanner(new File("src/prepositions.txt"));
							    while(scc.hasNext()){
							            String ln = scc.nextLine().toLowerCase().toString();
							            if(ln.matches(".*\\b" + check[k] + "\\b.*")) {
							            	
							            	preposition=true;
							            	//printedpreposition = true;
							            }else if(k>0&&ln.matches(".*\\b" + check[k-1] + "\\b.*")) {
							            	//System.out.println(check[k]+" "+check[k-1]);
							            	printedpreposition = true;
							            }
							        }
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                           
							if(preposition) {
								System.out.print(gettranslated(check[k].toLowerCase(),"PREPOSITION")+" ");
								preposition=false; 
							}else {	
							System.out.print(gettranslated(check[k],table)+" ");
							}
						}
						
					}					
				}else {
			    for (k = k; k>=0; k--) {
			//for (k = 0; k < check.length; k++) {

				if (check[k] != null) {
                    
					if(table=="AUXILIARY"){
						
						aux=check[k];
					}
					//System.out.print(check[k] + " ");
					System.out.print(gettranslated(check[k],table)+" ");

				}

			}
				}
			}
		} catch (Exception ex) {

			System.out.println(ex);

		}
	}
	public static void connection(){
		try {
			c=DriverManager.getConnection("jdbc:sqlite::resource:translateenbn.db");
			//System.out.println("Database opened successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
	public static String gettranslated(String English, String table){
		
		connection();
	    String  bn="";
	    String getverbandform[];
	    String form="";
        try{
        //System.out.println(English+" "+table);
        Statement statement = c.createStatement();
        ResultSet rs=null;
        if(table=="VERB") {
        	   getverbandform=English.split(",");
               form=getverbandform[0];
               English=getverbandform[1];
        }
        if(person=="secondperson" && table=="VERB") {
        English=English+person;	
        rs = statement.executeQuery( "SELECT * FROM "+table+" WHERE `English`='"+English+"';" );
        }else if(person=="thirdperson" && table=="VERB") {
        English=English+person;	
        rs = statement.executeQuery( "SELECT * FROM "+table+" WHERE `English`='"+English+"';" );	
        }else{
        rs = statement.executeQuery( "SELECT * FROM "+table+" WHERE `English`='"+English+"';" );       	
        }//rs.next();
        //System.out.print(rs.getString(2));
        while ( rs.next() ) {
         //System.out.println(English+" "+table);
         if(table=="VERB"){	
           //System.out.println(aux);
           //System.out.println(rs.getString("Present"));	 
           /*if(aux.toLowerCase().equals("will")||aux.toLowerCase().equals("shall")){	 
              bn = rs.getString("Future");
           }else if(aux.toLowerCase().equals("was")||aux.toLowerCase().equals("were")){
        	  bn = rs.getString("Pastcontinuous");
           }else if(aux.toLowerCase().equals("have")||aux.toLowerCase().equals("has")){
         	  bn = rs.getString("Presentperfect");
           }else if(aux.toLowerCase().equals("had")){
          	  bn = rs.getString("Pastperfect");
            }else{
        	  bn = rs.getString("Present");
           }*/
        	  if(printedpreposition) {
          		  bn = rs.getString("Prepositional"); 
          		  printedpreposition = false;
          	  }else if(form=="future"){	 
                 bn = rs.getString("Future");
              }else if(form=="pastcontinuous"){
           	  bn = rs.getString("Pastcontinuous");
              }else if(form=="presentperfect"){
            	  bn = rs.getString("Presentperfect");
              }else if(form=="pastperfect"){
             	  bn = rs.getString("Pastperfect");
              }else if(form=="infinite"){
              	  bn = rs.getString("Infinite");
              }else{
           	  bn = rs.getString(form);
              }	 
           
         }else{
        	 if(printedpreposition) {
       		  bn = rs.getString("Prepositional"); 
       		  printedpreposition = false;
       	      }else { 	 
              bn = rs.getString("Bangla");
       	      }
         }
        }
        c.close();
        
        }catch(Exception ex){
        	
        	ex.printStackTrace();
        }	
       
        return bn;
        
}
	
	
}
