import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;


public class LR {
	double rate;
	LinkedList<Integer []> trainset1;
	LinkedList<Integer []> trainset7;
	double [] weights;
	public LR(){

		BufferedReader br=null;
		String line;
		 trainset1=new LinkedList<Integer []>();
		 trainset7=new LinkedList<Integer []>();
		 weights=new double[785];
		 Random r = new Random();
		 for(int i=0;i<=784;i++){// Αρχικοποιούνται τα βάρη χρησιμοποιώντας την Gaussian κατανομή.
			 weights[i]=(r.nextGaussian());
		 }
		 r=null;
		try {
			br = new BufferedReader(new FileReader(new File("train1.txt")));
			while ((line = br.readLine()) != null) { // Διαβάζουμε τα δεδομένα εκπαίδευσης για την κατηγορία 1
				String[] t = line.split(" ");
				Integer[] intline = new Integer[785];
				intline[784]=1;
				for(int kk=0;kk<784;kk++){
					intline[kk]=Integer.valueOf(t[kk]);
				}
				trainset1.add(intline);
			}
			br.close();
			BufferedReader br2 = new BufferedReader(new FileReader(new File("train7.txt")));
			line=" ";
			while ((line = br2.readLine()) != null) { // Διαβάζουμε τα δεδομένα εκπαίδευσης για την κατηγορία 2
				String[] t = line.split(" ");
				Integer[] intline = new Integer[785];
				intline[784]=1;
				for(int kk=0;kk<784;kk++){
					intline[kk]=Integer.valueOf(t[kk]);
				}
				trainset7.add(intline);
			}
			br2.close();
			rate=0.0001; //Το learning Rate του αλγόριθμου που υλοποιήσαμε.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	/**
	 * 
	 * @param r Το γινόμενο των διανυσμάτων x και w.
	 * @return	Την πιθανότητα να ανήκει στην θετική κατηγορία
	 */
	public double logisticFunction(double r){
		return 1/(1+Math.exp(-r));
	}
	/**
	 * Εκπαιδεύει τον ταξινομητή μας.Η εκπαίδευση τελιώνει όταν 
	 * η νόρμα δυο της μεταβολής των βαρών πέσει κάτω από το 0.05 
	 */
	public void train(){

		double norm=1.0;
		int count =1;
		double [] oldweights=weights.clone();// Κρατάμε τα παλιά βάρη
		while(norm>0.05){
			double [] normfact=new double[weights.length];//Ένας πίνακας που θα έχει τους όρους για την νορμαλοποίηση 
			double weightM = magnitudeCalc(weights);//weightM=||w||
			for(int k=0;k<normfact.length;k++){
				normfact[k]=(0.1/(2)/weightM)*weights[k];
			}
			for(int i=0;i<trainset1.size();i++){//Αρχίζουμε την εκπαίδευση 
				Integer [] x=trainset1.get(i);
				double prediction = P(x);
				 for (int j=0; j<weights.length; j++) {
					weights[j] = weights[j] + rate * (1 - prediction) * x[j];//Η πρώτη κατηγορία είναι και η θετική 
				}
			}
			for(int i=0;i<trainset7.size();i++){
				Integer [] x=trainset7.get(i);
				double prediction = P(x);
				 for (int j=0; j<weights.length; j++) {
					 weights[j] = weights[j] + rate * (0 - prediction) * x[j];//Η δεύτερη είναι η αρνητική 
				}
			}
			for(int i=0;i<weights.length;i++){
				weights[i]=weights[i]-normfact[i]; //Πραγματοποιείται η νορμαλοποίηση των βαρών.
			}
			norm=norm(weights,oldweights);//Υπολογίζεται η νόρμα 2 της μεταβολής 
			oldweights=weights.clone();//Ανανεώνουμε τα παλιά βάρη.
			count++;
			System.out.println("iteration: "+count+" norm2: "+norm);
			/* Εδώ πραγματοποιείται ένα τέχνασμα για να γίνει η διαδικασία πιο γρήγορα .
			 * Αφού ο αλγόριθμος έχει κάνει ήδη κάποια βήματα θα πρέπει να ρίξουμε λιγάκι 
			 * το learning rate με σκοπό να προσεγγίσουμε καλύτερα τα βέλτιστα βάρη 
			 */
			
			if(count==100){
				rate=rate/10;
			}
			if(count==200){
				rate=rate/10;
			}
			if(count==250){
				rate=rate/10;
			}
			if(count==300){
				rate=rate/10;
			}
		}
		try{
		weightPrint();
		}catch(Exception e){
			
		}
	}
	/**
	 * 
	 * @param Ένα διάνυσμα από double 
	 * @return Το μέτρο του διανύσματος 
	 */
	public double magnitudeCalc(double[] vector) {
		double sum = 0;
		for(int i=0;i<vector.length;i++){
			sum=sum+Math.pow(vector[i],2);
		}
		return Math.sqrt(sum);
		
	}
	/**
	 * Εκτυπώνει σε ένα αρχείο με όνομα weights.txt 
	 * τα βάρη που έχουμε υπολογίσει αυτή την στιγμή 
	 * @throws IOException
	 */
	public void weightPrint() throws IOException{
		BufferedWriter writer =new BufferedWriter(new FileWriter("weights.txt"));
		for(int i=0;i<weights.length;i++){
			writer.write(Double.toString(weights[i])+'\n');
		}
		writer.close();
		}
	/**
	 * 
	 * @param x Ένα διάνυσμα των δεδομένων μας 
	 * @return Την πιθανότητα να ανήκει στην θετική κατηγορία
	 */
	public double P(Integer [] x){
		 double mult=0.0;
		 for (int i=0; i<weights.length;i++) {
			 mult =mult+ weights[i] * x[i];
		 }
		 return logisticFunction(mult);
	}
	/**
	 * 
	 * @param arg1 Ένα διάνυσμα από Double
	 * @param arg2 Ένα διάνυσμα από Double
	 * @return Την νόρμα 2 της διαφοράς τους.
	 */
	public double norm(double [] arg1,double [] arg2){
		double sum=0;
		for(int i=0;i<arg1.length;i++){
			sum=sum+(Math.pow(arg1[i]-arg2[i],2));
		}
		return Math.sqrt(sum);
	}

	/**
	 * 
	 * @param image Μια εικόνα που θέλουμε να γράψουμε ως αρχείο png 
	 * @param name Το όνομα του αρχείου 
	 */
	public void imageWriter(ImageMNIST image,String name){
		try{
		int sz = 28;
	    byte[] buffer = new byte[sz * sz];

	    for (int i = 0; i < sz; i++) {
	        for (int j = 0; j < sz; j++) {
	            buffer[(i * sz) + j] =d.fromIntToByte(image.pic[i][j]);
	        }
	    }
	    ImageIO.write(BMP.getGrayscale(28, buffer), "PNG", new File(name));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Ταξινομεί τα δεδομένα ελέγχου.
	 * Εκτυπώνονται τα ποσοστά της κάθε κατηγορίας.  
	 */
	public void classify(){
		System.out.println("----------------------------------------------------------------");
		String line;
		trainset1=null; // Δεν μας χρειάζονται τα δεδομένα εκπαίδευσης, αποδεσμεύεσαι την μνήμη 
		trainset7=null;
		try {
			int counter=0;
			int cor = 0;
			BufferedReader	br = new BufferedReader(new FileReader(new File("test1.txt")));
			while ((line = br.readLine()) != null) {//Διάβασε τα δεδομένα 
				String[] t = line.split(" ");
				Integer[] intline = new Integer[785];
				intline[784]=1;
				for(int kk=0;kk<784;kk++){
					intline[kk]=Integer.valueOf(t[kk]);
				}
				counter++;
				if(P(intline)>0.5){ //Αν η πιθανότητα είναι μεγαλύτερη από το 0.5 τότε βάλτο στην θετική κατηγορία 
					cor++; // Σωστά επιλέγεται 
				}
				else{
					imageWriter(new ImageMNIST(intline),"one"+counter+".png"); // Διαφορετικά γράψε την εικόνα που έκανε λάθος
				}
			}
			System.out.println("Accuracy for the possitive category: "+(double)cor/counter);
			br.close();
			 counter=0;
			 cor = 0;
			 line="";
			BufferedReader	br2 = new BufferedReader(new FileReader(new File("test7.txt")));
			while ((line = br2.readLine()) != null) {
				String[] t = line.split(" ");
				Integer[] intline = new Integer[785];
				intline[784]=1;
				for(int kk=0;kk<784;kk++){
					intline[kk]=Integer.valueOf(t[kk]);
				}
				counter++;
				if(P(intline)<0.5){ //Αν η πιθανότητα είναι μικρότερη από το 0.5 τότε βάλτο στην αρνητική κατηγορία 
					cor++;
				}
				else{
					imageWriter(new ImageMNIST(intline),"seven"+counter+".png"); // Διαφορετικά γράψε την εικόνα που έκανε λάθος
				}
			}
			System.out.println("Accuracy for the negative category: "+(double)cor/counter);
			br2.close();
		}
			catch(IOException e){
				System.out.println(e.toString());
			}
		}
public static void main(String [] args){
		LR l=new LR();
		l.train();
		l.classify();
	}
}
