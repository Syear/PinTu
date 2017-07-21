package com.example.pintu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** ���ö�λ���鴴��С����*/
	private ImageView[][] gameIV_array=new ImageView[3][5];
	/**��Ϸ������*/
	private GridLayout gameLayout;
	/**��ǰ�շ����ʵ���ı���*/
	private ImageView nullIV;
	/**��ǰ����*/
	private GestureDetector mDetector;
	/**�ж���Ϸ�Ƿ�ʼ*/
	private boolean isGameStart=false;
	
	@Override
	//���������¼�
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mDetector.onTouchEvent(event);
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����ʶ��
        mDetector=new GestureDetector(this, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO Auto-generated method stub
				int type=getDirByGes(arg0.getX(), arg0.getY(), arg1.getX(), arg1.getY());
				changeByDir(type);
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        setContentView(R.layout.activity_main);
        /*��ʼ����ϷС����*/
        //��ȡһ�Ŵ�ͼ
        Bitmap bigBm=((BitmapDrawable)getResources().getDrawable(R.drawable.jiahe)).getBitmap();
        int bmWAH=bigBm.getWidth()/5;//ÿ��С����Ŀ��
        
        for(int i=0;i<gameIV_array.length;i++){
        	for(int j=0;j<gameIV_array[0].length;j++){
        		//�����к����г��г����ɸ�СͼƬ
        		Bitmap bm=Bitmap.createBitmap(bigBm,j*bmWAH,i*bmWAH,bmWAH,bmWAH);
        		gameIV_array[i][j]=new ImageView(this);
        		gameIV_array[i][j].setImageBitmap(bm);//����ÿ��С�����ͼƬ
        		gameIV_array[i][j].setPadding(2, 2, 2, 2);//���÷���ļ��
        		gameIV_array[i][j].setTag(new GameData(i, j, bm));//���Զ��������
        		gameIV_array[i][j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						boolean flag=isHasByNullImageView((ImageView)v);
						//Toast.makeText(MainActivity.this, "�Ƿ���λ�ù�ϵ="+flag, Toast.LENGTH_SHORT).show();
						if(flag){
							changeDateByImageView((ImageView)v);
						}
					}
				});
        	}
        }
        //��ʼ����Ϸ���棬�����С����
        gameLayout=(GridLayout) findViewById(R.id.Layout);
        for(int i=0;i<gameIV_array.length;i++){
        	for(int j=0;j<gameIV_array[0].length;j++){
        		gameLayout.addView(gameIV_array[i][j]);
        	}
        }
        /*�������һ�������ǿյ�*/
        setNullImageView(gameIV_array[2][4]);
        /*��ʼ���������˳��*/
        randomMove();
        isGameStart=true;//��ʼ״̬
        }
    private void changeByDir(int type){
    	changeByDir(type,true);
    }
    /**
     *  �������Ƶķ����ȡ�շ������ڵ�λ�� ��������ڷ���������ƶ�
     * @param type 1:��  2����  3����  4����
     */
    private void 	changeByDir(int type,boolean isAnim){
    	//��ȡ�շ����λ��
    	GameData mNullGameDate=(GameData) nullIV.getTag();
    	int new_x=mNullGameDate.x;
    	int new_y=mNullGameDate.y;
    	
    	//���ݷ���������Ӧ���ƶ�λ������
    	if(type==1){
    		new_x++;
    	}else if(type==2){
    		new_x--;
    	}else if(type==3){
    		new_y++;
    	}else if(type==4){
    		new_y--;
    	}
    	//�жϸ÷����Ƿ����
    	if(new_x>=0&&new_x<gameIV_array.length&&new_y>=0&&new_y<gameIV_array[0].length){
    		if(isAnim){
    			changeDateByImageView(gameIV_array[new_x][new_y]);
    		}else{
    			changeDateByImageView(gameIV_array[new_x][new_y],isAnim);
    		}
    			
    		
    	}else{
    		//ʲô������
    	}
    	//����������ƶ�
    }
    /**
     * �����ж� 
     * @param start_x ������ʼ��X
     * @param start_y ������ʼ��Y
     * @param end_x �����յ�X
     * @param end_y	�����յ�X
     * @return 1:��  2����  3����  4����
     */
    private int getDirByGes(float start_x,float start_y,float end_x,float end_y){
    	boolean isLeftOrRight=(Math.abs(start_x-end_x)>Math.abs(start_y-end_y)?true:false);
    	if(isLeftOrRight){
    		boolean isLeft=start_x-end_x>0?true:false;
    		if(isLeft){
    			return 3;
    		}else{
    			return 4;
    		}
    	}else{
    		boolean isUp=start_y-end_y>0?true:false;
    		if(isUp){
    			return 1;
    		}else{
    			return 2;
    		}
    	}
    }
    public void changeDateByImageView(final ImageView mImageView)
    {
    	changeDateByImageView(mImageView,true);
    }
    /**
     * ���ö�������֮�󣬽����������������
     * @param mImageView �����VIEW
     * @param true:�ж��� false���޶���
     */
    public void changeDateByImageView(final ImageView mImageView,boolean isAnim){  	
    	if(!isAnim){
    		GameData mGameData=(GameData) mImageView.getTag();
			nullIV.setImageBitmap(mGameData.bm);
			GameData mNullGameData=(GameData) nullIV.getTag();
			mNullGameData.bm=mGameData.bm;
			mNullGameData.p_x=mGameData.p_x;
			mNullGameData.p_y=mGameData.p_y;
			//���õ�ǰ������ǿշ���
			setNullImageView(mImageView);
			if(isGameStart){
	    		isGameOver();//�ɹ��ǻᵯ��Toast
	    	}
    		return;
    	}
    	//����һ������
    	TranslateAnimation translateAnimation=null;
    	if(mImageView.getX()>nullIV.getX()){//��������ڿշ�����±�
    		//����
    		translateAnimation=new TranslateAnimation(0.1f,-mImageView.getWidth() ,0.1f,0.1f);
    	}else if(mImageView.getX()<nullIV.getX()){//��������ڿշ�����ϱ�
    		//����
    		translateAnimation=new TranslateAnimation(0.1f,mImageView.getWidth() ,0.1f,0.1f);
    	}else if(mImageView.getY()>nullIV.getY()){//��������ڿշ�����ұ�
    		//����
    		translateAnimation=new TranslateAnimation(0.1f,0.1f ,0.1f,-mImageView.getWidth());
    	}else if(mImageView.getY()<nullIV.getY()){//��������ڿշ�������
    		//����
    		translateAnimation=new TranslateAnimation(0.1f,0.1f ,0.1f,mImageView.getWidth());
    	}
    	//���ö���ʱ��
    	translateAnimation.setDuration(70);
    	//���ö���������ͣ��
    	translateAnimation.setFillAfter(true);
    	//��������
    	translateAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				mImageView.clearAnimation();//�������Ч��
				GameData mGameData=(GameData) mImageView.getTag();
				nullIV.setImageBitmap(mGameData.bm);
				GameData mNullGameData=(GameData) nullIV.getTag();
				mNullGameData.bm=mGameData.bm;
				mNullGameData.p_x=mGameData.p_x;
				mNullGameData.p_y=mGameData.p_y;
				//���õ�ǰ������ǿշ���
				setNullImageView(mImageView);
				if(isGameStart){
		    		isGameOver();//�ɹ��ǻᵯ��Toast
		    	}
			}
		});
    	mImageView.startAnimation(translateAnimation);
    }
    /**
     * ����һ���շ���
     * @param mImageView=Ҫ����Ϊ�յ�ʵ��
     */
    private void setNullImageView(ImageView mImageView){
    	mImageView.setImageBitmap(null);
    	this.nullIV=mImageView;
    }
    //�������˳��
    public void randomMove(){
    	//���ҵĴ���
    	for(int i=0;i<10;i++){
    		//�������ƿ�ʼ����
    		int type=(int) ((Math.random()*4)+1);
    		changeByDir(type, false);
    		
    	}
    	//��ʼ����
    }
    /**
     * �жϵ�ǰ����ķ����ǲ��ǿշ�������
     * @param mImageView ������ķ���
     * @return true:���� ��false:������
     */
    private boolean isHasByNullImageView(ImageView mImageView) {
    	//�ֱ��ȡ��ǰ�շ����λ�����������λ�ã�ͨ��X,Y���߶���һ�ķ�ʽ�ж�
    	GameData mnullGameData=(GameData) nullIV.getTag();
    	GameData mGameData=(GameData) mImageView.getTag(); 
    	if(mnullGameData.y==mGameData.y&&mGameData.x+1==mnullGameData.x){//��������ڿշ�����ϱ�
    		return true;
    	}else if(mnullGameData.y==mGameData.y&&mGameData.x-1==mnullGameData.x){//��������ڿշ�����±�
    		return true;
    	}else if(mnullGameData.x==mGameData.x&&mGameData.y+1==mnullGameData.y){//��������ڿշ�������
    		return true;
    	}else if(mnullGameData.x==mGameData.x&&mGameData.y-1==mnullGameData.y){//��������ڿշ�����ұ�
    		return true;
    	}
    	return false;
    }
    
    /**ÿ����ϷС������Ҫ�󶨵�����*/
    class GameData{
    	//ÿ��С�����ʵ��λ��
    	public int x=0;
    	public int y=0;
    	public Bitmap bm;
    	public int p_x=0;
    	public int p_y=0;
		public GameData(int x, int y, Bitmap bm) {
			super();
			this.x = x;
			this.y = y;
			this.bm = bm;
			this.p_x = x;
			this.p_y = y;
		}
		/**
		 * �ж�ÿ��С�����λ���Ƿ���ȷ
		 * @return true:��ȷ false:����
		 */
		public boolean isTrue() {
			// TODO Auto-generated method stub
			if(x==p_x&&y==p_y){
				return true; 
			}
			return false;
		}
    	
    }
    //�ж���Ϸ�����ķ���
    public void isGameOver(){
    	boolean isGameOver=true;
    	//����ÿһ��С����
    	 for(int i=0;i<gameIV_array.length;i++){
         	for(int j=0;j<gameIV_array[0].length;j++){
         			//Ϊ�յķ�������
         		if(gameIV_array[i][j]==nullIV){
         			continue;
         		}
         		GameData mGameData=(GameData) gameIV_array[i][j].getTag();
         		if(!mGameData.isTrue()){
         			isGameOver=false;
         			break;
         		}
         	}
         }
    	 
    
    	//����һ�����ر���������Ϸ�Ƿ����������ʱ����ʾ��
    	 if(isGameOver){
    		 Toast.makeText(MainActivity.this, "��Ϸ����", Toast.LENGTH_LONG).show();
    	 }
    }
    }


  