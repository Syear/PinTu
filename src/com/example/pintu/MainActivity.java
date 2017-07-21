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
	/** 利用二位数组创建小方块*/
	private ImageView[][] gameIV_array=new ImageView[3][5];
	/**游戏主界面*/
	private GridLayout gameLayout;
	/**当前空方块的实例的保存*/
	private ImageView nullIV;
	/**当前手势*/
	private GestureDetector mDetector;
	/**判断游戏是否开始*/
	private boolean isGameStart=false;
	
	@Override
	//监听触摸事件
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mDetector.onTouchEvent(event);
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //手势识别
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
        /*初始化游戏小方块*/
        //获取一张大图
        Bitmap bigBm=((BitmapDrawable)getResources().getDrawable(R.drawable.jiahe)).getBitmap();
        int bmWAH=bigBm.getWidth()/5;//每个小方块的宽高
        
        for(int i=0;i<gameIV_array.length;i++){
        	for(int j=0;j<gameIV_array[0].length;j++){
        		//根据行和列切成切成若干个小图片
        		Bitmap bm=Bitmap.createBitmap(bigBm,j*bmWAH,i*bmWAH,bmWAH,bmWAH);
        		gameIV_array[i][j]=new ImageView(this);
        		gameIV_array[i][j].setImageBitmap(bm);//设置每个小方块的图片
        		gameIV_array[i][j].setPadding(2, 2, 2, 2);//设置方块的间距
        		gameIV_array[i][j].setTag(new GameData(i, j, bm));//绑定自定义的数据
        		gameIV_array[i][j].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						boolean flag=isHasByNullImageView((ImageView)v);
						//Toast.makeText(MainActivity.this, "是否有位置关系="+flag, Toast.LENGTH_SHORT).show();
						if(flag){
							changeDateByImageView((ImageView)v);
						}
					}
				});
        	}
        }
        //初始化游戏界面，并添加小方块
        gameLayout=(GridLayout) findViewById(R.id.Layout);
        for(int i=0;i<gameIV_array.length;i++){
        	for(int j=0;j<gameIV_array[0].length;j++){
        		gameLayout.addView(gameIV_array[i][j]);
        	}
        }
        /*设置最后一个方块是空的*/
        setNullImageView(gameIV_array[2][4]);
        /*初始化随机打乱顺序*/
        randomMove();
        isGameStart=true;//开始状态
        }
    private void changeByDir(int type){
    	changeByDir(type,true);
    }
    /**
     *  根据手势的方向获取空方块相邻的位置 ，如果存在方块则进行移动
     * @param type 1:上  2：下  3：左  4：右
     */
    private void 	changeByDir(int type,boolean isAnim){
    	//获取空方块的位置
    	GameData mNullGameDate=(GameData) nullIV.getTag();
    	int new_x=mNullGameDate.x;
    	int new_y=mNullGameDate.y;
    	
    	//根据方向设置相应的移动位置坐标
    	if(type==1){
    		new_x++;
    	}else if(type==2){
    		new_x--;
    	}else if(type==3){
    		new_y++;
    	}else if(type==4){
    		new_y--;
    	}
    	//判断该方块是否存在
    	if(new_x>=0&&new_x<gameIV_array.length&&new_y>=0&&new_y<gameIV_array[0].length){
    		if(isAnim){
    			changeDateByImageView(gameIV_array[new_x][new_y]);
    		}else{
    			changeDateByImageView(gameIV_array[new_x][new_y],isAnim);
    		}
    			
    		
    	}else{
    		//什么都不做
    	}
    	//存在则进行移动
    }
    /**
     * 手势判断 
     * @param start_x 手势起始点X
     * @param start_y 手势起始点Y
     * @param end_x 手势终点X
     * @param end_y	手势终点X
     * @return 1:上  2：下  3：左  4：右
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
     * 利用动画结束之后，交换两个方块的数据
     * @param mImageView 点击的VIEW
     * @param true:有动画 false：无动画
     */
    public void changeDateByImageView(final ImageView mImageView,boolean isAnim){  	
    	if(!isAnim){
    		GameData mGameData=(GameData) mImageView.getTag();
			nullIV.setImageBitmap(mGameData.bm);
			GameData mNullGameData=(GameData) nullIV.getTag();
			mNullGameData.bm=mGameData.bm;
			mNullGameData.p_x=mGameData.p_x;
			mNullGameData.p_y=mGameData.p_y;
			//设置当前点击的是空方块
			setNullImageView(mImageView);
			if(isGameStart){
	    		isGameOver();//成功是会弹出Toast
	    	}
    		return;
    	}
    	//创建一个动画
    	TranslateAnimation translateAnimation=null;
    	if(mImageView.getX()>nullIV.getX()){//点击方块在空方块的下边
    		//上移
    		translateAnimation=new TranslateAnimation(0.1f,-mImageView.getWidth() ,0.1f,0.1f);
    	}else if(mImageView.getX()<nullIV.getX()){//点击方块在空方块的上边
    		//下移
    		translateAnimation=new TranslateAnimation(0.1f,mImageView.getWidth() ,0.1f,0.1f);
    	}else if(mImageView.getY()>nullIV.getY()){//点击方块在空方块的右边
    		//左移
    		translateAnimation=new TranslateAnimation(0.1f,0.1f ,0.1f,-mImageView.getWidth());
    	}else if(mImageView.getY()<nullIV.getY()){//点击方块在空方块的左边
    		//右移
    		translateAnimation=new TranslateAnimation(0.1f,0.1f ,0.1f,mImageView.getWidth());
    	}
    	//设置动画时常
    	translateAnimation.setDuration(70);
    	//设置动画结束后停留
    	translateAnimation.setFillAfter(true);
    	//交换数据
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
				mImageView.clearAnimation();//清除动画效果
				GameData mGameData=(GameData) mImageView.getTag();
				nullIV.setImageBitmap(mGameData.bm);
				GameData mNullGameData=(GameData) nullIV.getTag();
				mNullGameData.bm=mGameData.bm;
				mNullGameData.p_x=mGameData.p_x;
				mNullGameData.p_y=mGameData.p_y;
				//设置当前点击的是空方块
				setNullImageView(mImageView);
				if(isGameStart){
		    		isGameOver();//成功是会弹出Toast
		    	}
			}
		});
    	mImageView.startAnimation(translateAnimation);
    }
    /**
     * 设置一个空方块
     * @param mImageView=要设置为空的实例
     */
    private void setNullImageView(ImageView mImageView){
    	mImageView.setImageBitmap(null);
    	this.nullIV=mImageView;
    }
    //随机打乱顺序
    public void randomMove(){
    	//打乱的次数
    	for(int i=0;i<10;i++){
    		//根据手势开始交换
    		int type=(int) ((Math.random()*4)+1);
    		changeByDir(type, false);
    		
    	}
    	//开始交换
    }
    /**
     * 判断当前点击的方块是不是空方块相邻
     * @param mImageView 所点击的方块
     * @return true:相邻 ；false:不相邻
     */
    private boolean isHasByNullImageView(ImageView mImageView) {
    	//分别获取当前空方块的位置与点击方块的位置，通过X,Y两边都差一的方式判断
    	GameData mnullGameData=(GameData) nullIV.getTag();
    	GameData mGameData=(GameData) mImageView.getTag(); 
    	if(mnullGameData.y==mGameData.y&&mGameData.x+1==mnullGameData.x){//点击方块在空方块的上边
    		return true;
    	}else if(mnullGameData.y==mGameData.y&&mGameData.x-1==mnullGameData.x){//点击方块在空方块的下边
    		return true;
    	}else if(mnullGameData.x==mGameData.x&&mGameData.y+1==mnullGameData.y){//点击方块在空方块的左边
    		return true;
    	}else if(mnullGameData.x==mGameData.x&&mGameData.y-1==mnullGameData.y){//点击方块在空方块的右边
    		return true;
    	}
    	return false;
    }
    
    /**每个游戏小方块上要绑定的数据*/
    class GameData{
    	//每个小方块的实际位置
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
		 * 判断每个小方块的位置是否正确
		 * @return true:正确 false:错误
		 */
		public boolean isTrue() {
			// TODO Auto-generated method stub
			if(x==p_x&&y==p_y){
				return true; 
			}
			return false;
		}
    	
    }
    //判断游戏结束的方法
    public void isGameOver(){
    	boolean isGameOver=true;
    	//遍历每一个小方块
    	 for(int i=0;i<gameIV_array.length;i++){
         	for(int j=0;j<gameIV_array[0].length;j++){
         			//为空的方块跳过
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
    	 
    
    	//根据一个开关变量决定游戏是否结束，结束时有提示。
    	 if(isGameOver){
    		 Toast.makeText(MainActivity.this, "游戏结束", Toast.LENGTH_LONG).show();
    	 }
    }
    }


  