import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class TareaJuegoVida implements Runnable{
	int[][] reticulaBase_,reticulaGenerada_;
	int inicio_,fin_,height_,width_;
	CyclicBarrier barrera_;
	AtomicInteger living_;

	TareaJuegoVida(int[][] anteriorGen,int[][] nuevaGen,int inicio,int fin,CyclicBarrier barrera,int Alto,int Ancho,AtomicInteger viv){
		reticulaBase_=anteriorGen;
		reticulaGenerada_=nuevaGen;
		inicio_=inicio;
		fin_=fin;
		barrera_=barrera;
		height_=Alto;//height es el número de filas al fin y al cabo
		width_=Ancho;
		living_=viv;
	}


	int Nvecinas(int i, int j)
	{
		int nvecinas=0;
		if(i>=1)
		{
			nvecinas+=reticulaBase_[i-1][j];
			if(j<=height_-2)
				nvecinas+=reticulaBase_[i-1][j+1];
			if(j>=1)
				nvecinas+=reticulaBase_[i-1][j-1];
		}
		if(i<=width_-2)
		{
			nvecinas+=reticulaBase_[i+1][j];
			if(j<=height_-2)
				nvecinas+=reticulaBase_[i+1][j+1];
			if(j>=1)
				nvecinas+=reticulaBase_[i+1][j-1];	
		}	
		if(j>=1)
			nvecinas+=reticulaBase_[i][j-1];
		if(j<=height_-2)
			nvecinas+=reticulaBase_[i][j+1];

		return nvecinas;
	}

	public void run()
	{
		for(int i=inicio_;i<fin_;++i)
		{
			for(int j=0;j<height_;++j)
			{
				int vecindad=Nvecinas(i,j);
				if(reticulaBase_[i][j]==1)
				{
					if(vecindad<2 || vecindad>3)
						reticulaGenerada_[i][j]=0;
					else
						reticulaGenerada_[i][j]=1;
				}
				else if(vecindad==3)
					reticulaGenerada_[i][j]=1;
			}
		}

		try{
			barrera_.await();
		}catch(BrokenBarrierException e){ e.getMessage(); }
		 catch(InterruptedException e){ e.getMessage(); }

		for(int i=inicio_;i<fin_;++i)
		{
			for(int j=0;j<height_;++j)
			{
				reticulaBase_[i][j]=reticulaGenerada_[i][j];
				if(reticulaBase_[i][j]==1)
					living_.incrementAndGet();
			}
		}
	}
}