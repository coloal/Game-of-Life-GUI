import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class automataJuegoVida{
		public int[][] reticulaInicial,reticulaNext;
		private static randomGenerator rand = new randomGenerator();
		private static BigInteger dos = new BigInteger("2");
		private static int nucleos=Runtime.getRuntime().availableProcessors();
		int ventana,AltoReticula_,AnchoReticula_;
		CyclicBarrier barrera,barreraSecuencial;
		AtomicInteger vivas_;
		int generacion_;
		public static long tiempoTotal=0,tiempoTotalSec=0;
		automataJuegoVida(BigInteger semilla, int AltoReticula, int AnchoReticula)
		{
			generacion_=0;
			vivas_=new AtomicInteger(0);
			reticulaInicial = new int[AltoReticula][AnchoReticula];
			reticulaNext = new int[AltoReticula][AnchoReticula];
			AnchoReticula_ = AnchoReticula;
			AltoReticula_ = AltoReticula;

			for(int i=0; i<AltoReticula ;++i)
			{
				for(int j=0; j<AnchoReticula ;++j)
				{
					semilla=rand.fishman_moore1(semilla);
					reticulaInicial[i][j]=semilla.mod(dos).intValue();
					if(reticulaInicial[i][j]==1)
						vivas_.incrementAndGet();
				}
			}
			ventana = (int)(AnchoReticula/nucleos);
			barrera = new CyclicBarrier(nucleos);
			barreraSecuencial = new CyclicBarrier(1);
		}

		int Nvivas(){return vivas_.get();}
		int gen(){return generacion_;}
		void nextGen()
		{
			++generacion_;
			vivas_=new AtomicInteger(0);
			ThreadPoolExecutor ejecutor = new ThreadPoolExecutor(nucleos,nucleos,0L,TimeUnit.MILLISECONDS
				,new LinkedBlockingQueue<Runnable>());

			int inicio=0,fin=ventana;

			long inicTiempo = System.nanoTime();
			for(int i = 0; i<=nucleos-1 ; ++i)
	        {    
	        	ejecutor.execute(new TareaJuegoVida(reticulaInicial,reticulaNext,inicio,fin,barrera,AltoReticula_,AnchoReticula_,vivas_));
	            inicio += ventana;
	            fin += ventana;
	        }
			try
			{
	          ejecutor.shutdown();
	          ejecutor.awaitTermination(1L,TimeUnit.DAYS);
	        }catch(InterruptedException e){}
	        tiempoTotal = (System.nanoTime()-inicTiempo)/(long)1.0e3;
		}

		void nextGenSecuencial()
		{
			++generacion_;
			vivas_=new AtomicInteger(0);
			ThreadPoolExecutor ejecutor = new ThreadPoolExecutor(nucleos,nucleos,0L,TimeUnit.MILLISECONDS
				,new LinkedBlockingQueue<Runnable>());

			int inicio=0,fin=AnchoReticula_;

			long inicTiempo = System.nanoTime();
			for(int i = 0; i<=0 ; ++i)
	        {    
	        	ejecutor.execute(new TareaJuegoVida(reticulaInicial,reticulaNext,inicio,fin,barreraSecuencial,AltoReticula_,AnchoReticula_,vivas_));
	            inicio += ventana;
	            fin += ventana;
	        }
			try
			{
	          ejecutor.shutdown();
	          ejecutor.awaitTermination(1L,TimeUnit.DAYS);
	        }catch(InterruptedException e){}
	        tiempoTotalSec = (System.nanoTime()-inicTiempo)/(long)1.0e3;
		}

		public static void main(String[] args) {
			automataJuegoVida vida=new automataJuegoVida(new BigInteger("1"),50,50);
			
			for(int k=0;k<3;++k)
			{	
		        for(int i=0; i<50;++i)
		        {
		        	for(int j=0; j<50; ++j)
		        	{
		        		System.out.print(vida.reticulaNext[i][j]);
		        	}
		        	System.out.println();
	        	}
	        	vida.nextGen();
			}
		}
}