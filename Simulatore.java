import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;        
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.LayoutStyle.ComponentPlacement.*;
import javax.swing.*;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;        
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.LayoutStyle.ComponentPlacement.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.math.BigInteger;
import java.util.concurrent.*;
import java.lang.Math;


public class Simulatore implements ActionListener{
    static JFrame frame_inicio,frame_simulacion,frame_estadisticas;
    JTextField dimensionField;
    JPanel pane,statsPane,mainStats;
    GraphicPanelCellAutomata graphicPane;
    StatsPane stPane;
    int dimension;
    static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    JTextField cellsAlife_,speedUp_;

    Simulatore()
    {
      dimension=200;
    }

    public void actionPerformed(ActionEvent e){
      if("crear vida".equals(e.getActionCommand()))
      {
      	cellsAlife_ = new JTextField("0");

        frame_simulacion = new JFrame("Simulacion");
        frame_simulacion.setPreferredSize(new Dimension(dimension,dimension));     
        graphicPane = new GraphicPanelCellAutomata(dimension,cellsAlife_);
        frame_simulacion.setContentPane(graphicPane);
        frame_simulacion.pack();
        frame_simulacion.setVisible(true);

        frame_estadisticas = new JFrame("Stats");
        frame_estadisticas.setPreferredSize(new Dimension(dimension+150,150));
        stPane = new StatsPane(graphicPane.automata(),dimension);
        stPane.setPreferredSize(new Dimension(dimension,dimension));
        frame_estadisticas.setLocation(0,dimension);

        graphicPane.setStatsPane(stPane);

        statsPane = new JPanel(new BorderLayout());
        statsPane.add(cellsAlife_);
     
        mainStats = new JPanel(new BorderLayout());
        mainStats.add(statsPane,BorderLayout.CENTER);
        mainStats.add(stPane,BorderLayout.WEST);
        frame_estadisticas.setContentPane(mainStats);

        frame_estadisticas.pack();
        frame_estadisticas.setVisible(true);
      }
      else if("dimension".equals(e.getActionCommand()))
      {
        dimension=Integer.parseInt(dimensionField.getText());
      }
      else if("iniciar simulacion".equals(e.getActionCommand()))
      {
        graphicPane.dibujar(); 
      }
      else if("speedUP".equals(e.getActionCommand()))
      {
      	//speedUp_.setText(Integer.toString((int)automataJuegoVida.tiempoTotal));
      	automataJuegoVida aux=new automataJuegoVida(new BigInteger("1"),dimension,dimension);
      	aux.nextGenSecuencial();
      	double sp = (double)((double)aux.tiempoTotalSec/(double)aux.tiempoTotal);
        speedUp_.setText(Double.toString(sp));
      }
      else if("fin".equals(e.getActionCommand()))
      {
        graphicPane.parar();
      }
    }
   

    private void createPaneInicio()
    {
      pane = new JPanel(new BorderLayout());
      JButton simularButton = new JButton("crear vida"),iniciar_simul = new JButton("iniciar simulacion");
      JButton speedUP = new JButton("speedUP");
      JButton fin = new JButton("fin");
      JLabel spUp=new JLabel("speedup");
      JLabel dimen=new JLabel("dimension");
      speedUP.addActionListener(this);
      iniciar_simul.addActionListener(this);
      simularButton.addActionListener(this);
      fin.addActionListener(this);
      dimensionField = new JTextField("200");
      dimensionField.addActionListener(this);
      dimensionField.setActionCommand("dimension");
      speedUp_=new JTextField("0");

      GroupLayout layout = new GroupLayout(pane);
      layout.setAutoCreateGaps(true);
      layout.setAutoCreateContainerGaps(true);
      pane.setLayout(layout);

      layout.setHorizontalGroup(layout.createSequentialGroup()
      	.addGroup(layout.createParallelGroup(LEADING)
      	  .addComponent(dimen)
          .addComponent(spUp)
        )
        .addGroup(layout.createParallelGroup(LEADING)
          .addComponent(dimensionField)
          .addComponent(simularButton)
          .addComponent(iniciar_simul)
          .addComponent(speedUP)
          .addComponent(speedUp_)
          .addComponent(fin)
        )

      );      

      layout.setVerticalGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(dimen)
          .addComponent(dimensionField)
        )
        .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(simularButton)
        )
         .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(iniciar_simul)
        )
         .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(speedUP)
        )
         .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(spUp)
          .addComponent(speedUp_)
        )
         .addGroup(layout.createParallelGroup(BASELINE)
          .addComponent(fin)
        )
      );

      Border borde = BorderFactory.createLineBorder(new Color(51,134,202));
      TitledBorder borde_titulo = BorderFactory.createTitledBorder(borde,"Juego de la vida");
    
      pane.setBorder(borde_titulo);
      pane.setBackground(new Color(176,201,247));
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        Simulatore sim = new Simulatore();
        /*FRAME OPTIONS*/
        frame_inicio = new JFrame("SimulatoreInicio");
        frame_inicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_inicio.setPreferredSize(new Dimension(250,250));

        
        int x = (int)(dim.getWidth()/2);
        int y = (int)(dim.getHeight()/2);
        
        frame_inicio.setLocation(x,y);
        sim.createPaneInicio();
        frame_inicio.setContentPane(sim.pane);

        frame_inicio.pack();
        frame_inicio.setVisible(true);  
    }
 
    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

//PANEL PARA DIBUJAR RANDOM ***PRACTICA 6****
class GraphicPanelCellAutomata extends JPanel{
  private static final long serialVersionUID = 1L;//REVISAR
  int dimension_;
  automataJuegoVida automata_;
  static int i=0;
  StatsPane statsPane;
  int x_;
  JTextField txtF_;
  int generaciones;
  boolean verdad;
  public GraphicPanelCellAutomata(int dimension,JTextField txtF){
    super(new BorderLayout());
    dimension_ = dimension;
    automata_ =  new automataJuegoVida(new BigInteger("1"),dimension,dimension);
  	txtF_=txtF;
    generaciones=0;
    verdad=true;
  }

  void setStatsPane(StatsPane p){statsPane=p;}
  automataJuegoVida automata(){return automata_;}

  public void parar(){
        verdad=false;
    }
  public void dibujar()
  {
    Thread  updateThread = new Thread(){
      public void run(){
        while(verdad){
          automata_.nextGen();
          repaint();
          try{
            Thread.sleep(50);
          }catch(InterruptedException e){}
        }
      }
    };
    updateThread.start();
  }

  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);  

    for(int y=0; y<dimension_;++y)
    {
      for(int x=0; x<dimension_; ++x)
      {
        switch(automata_.reticulaInicial[y][x])
        {
          case 0:
            g.setColor(Color.black);
            g.fillRect(y,x,1,1);
            break;
          case 1:
            g.setColor(Color.white);
            g.fillRect(y,x,1,1);
            break;
        }
      }
    }
    ++generaciones;
    statsPane.paintPoblacion(statsPane.getGraphics(),generaciones);
    txtF_.setText(Integer.toString(automata_.Nvivas()));
  }

}

class StatsPane extends JPanel
{
  private static final long serialVersionUID = 1L;
  automataJuegoVida aut_;
  int dim_;
  int pobAux_;

  StatsPane(automataJuegoVida a,int d)
  {
    aut_=a;
    dim_=d;
  	pobAux_=0;
  }

  public void paintPoblacion(Graphics g,int gen)
  {
  	if(aut_.Nvivas()!=0)
  		pobAux_=1+getHeight()-(int)(150*((float)aut_.Nvivas()/(dim_*dim_)));
    
    g.fillRect(gen%dim_,pobAux_,1,1);
    

    if(gen%dim_ == dim_-1)
      g.clearRect(0,0,dim_,150);
  }
}