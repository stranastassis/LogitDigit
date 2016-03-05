
public class ImageMNIST {

	//int[] column;
	Integer[] row;
	public Integer[][] pic;
	public ImageMNIST(Integer[] intline) {
		super();
		pic=new Integer[28][28];
		this.row = intline;
		int counteri=0;
		int counterj=0;
		for(int qq=1;qq<intline.length+1;qq++){
			
			pic[counteri][counterj]=intline[qq-1];
			counterj++;	
			if(qq%28==0 && qq!=0){
				counterj=0;
				counteri++;
				
			}
			if(counteri==28 )
				break;
		}
		
		//System.out.println(pic);
	}
	
}
