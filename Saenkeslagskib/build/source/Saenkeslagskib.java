import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Saenkeslagskib extends PApplet {

Plade plade;
Knapper start;
Knapper exit;
Knapper spil_ovre;


enum Marker {
  STANDBY, NYTSPIL, STOPSPIL, POSITION, GAET
};


enum Error {
  VARIABLEERROR, OK
};


Marker marker;
Marker marker2;


Error error;


Tilstand vundet;

public void setup()
{
  
  background(0, 0, 0);
  frameRate(60);

  marker = Marker.STANDBY;
  marker2 = Marker.POSITION;

  error = Error.OK;

  vundet = Tilstand.UDEFINERET;

  plade = new Plade(10);
  start = new Knapper(125, 150, "Placer Skibe");
  exit = new Knapper(125, 214, "Luk Spil");
  spil_ovre = new Knapper(0, 500, "Start");
}

public void draw()
{
  background(105, 48, 10);


  if (marker == Marker.STANDBY)
  {

    start.display();
    exit.display();
    switch(vundet)
    {
    case SPILLER:
      fill(0);
      textSize(50);
      text("Du vandt :happyface:", 5, 140);
      break;
    case AI:
      fill(0);
      textSize(50);
      text("Du tabte :sadface:", 30, 140);
      break;
    }
  } else if (marker == Marker.NYTSPIL)
  {
    plade.display();
    spil_ovre.oplaast();
    vundet = Tilstand.UDEFINERET;
    if (marker2 == Marker.POSITION)
    {
      spil_ovre.display();
    }
  } else
  {
    fill(0);
    textSize(50);
    text("...", 150, 150);
  }

  switch (error)
  {
  case VARIABLEERROR:
    fill(0);
    textSize(20);
    text("Placer Flere Skibe!", 260, 550);
    break;
  }
}

public void mouseClicked()
{
  if (mouseButton == LEFT)
  {
    if (start.click())
    {
      start.displayEt();
      marker = Marker.NYTSPIL;
      start.laast();
      exit.laast();
    }

    if (exit.click())
    {
      exit.displayEt();
      marker = Marker.STOPSPIL;
      start.laast();
      exit.laast();
    }

    if (marker == Marker.NYTSPIL)
    {
      if (marker2 == Marker.POSITION)
      {
        plade.placerSkibe();
      } else if (marker2 == Marker.GAET)
      {
        plade.guess();
        switch(plade.vundet())
        {
        case SPILLER:
          marker = Marker.STANDBY;
          marker2 = Marker.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.SPILLER;
          plade.genstart();
          break;
        case AI:
          marker = Marker.STANDBY;
          marker2 = Marker.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.AI;
          plade.genstart();
          break;
        }
      }

      if (spil_ovre.click())
      {
        spil_ovre.displayEt();
        if (plade.getSpillerSkibe() == plade.MAXSKIBE())
        {
          spil_ovre.laast();
          marker2 = Marker.GAET;
          error = Error.OK;
          plade.genererAiSkibe();
        } else
        {
          error = Error.VARIABLEERROR;
        }
      }
    }
  }
}
class Knapper
{
  private float xAksen;
  private float yAksen;
  private float brede;
  private float hoejde;
  private String navn;
  private boolean tilstand;
  private PImage knapbilleder;
  private PImage [] piimgs;
  private int indeks;

  public Knapper(float xAksen, float yAksen, String navn)
  {
    this.xAksen = xAksen;
    this.yAksen = yAksen;
    this.navn = navn;
    brede = 200;
    hoejde = 50;
    tilstand = true;

    indeks = 0;
    knapbilleder = loadImage("img/knap.png");
    piimgs = new PImage[2];
    piimgs[0] = knapbilleder.get(0, 0, 255, 65);
    piimgs[1] = knapbilleder.get(0, 64, 255, 65);
  }

  public void display()
  {
    final float FORSKYDNING = 32;

    image(piimgs[0], xAksen, yAksen);
    fill(0);
    textSize(25);
    text(navn, xAksen+FORSKYDNING, yAksen+FORSKYDNING);
  }

  public void displayNull()
  {
    final float FORSKYDNING = 32;


    image(piimgs[0], xAksen, yAksen);
    fill(0);
    textSize(25);
    text(navn, xAksen+FORSKYDNING, yAksen+FORSKYDNING);
  }

  public void displayEt()
  {
    final float FORSKYDNING = 32;


    image(piimgs[1], xAksen, yAksen);
    fill(0);
    textSize(25);
    text(navn, xAksen+FORSKYDNING, yAksen+FORSKYDNING);
  }

  public boolean click()
  {
    if (tilstand && (mouseX >= xAksen) && (mouseX <= xAksen+brede)
      && (mouseY >= yAksen) && (mouseY <= yAksen + hoejde))
    {
      indeks = 1;
      return true;
    } else
    {
      indeks = 0;
      return false;
    }
  }

  public void laast()
  {
    tilstand = false;
  }

  public void oplaast()
  {
    tilstand = true;
  }
}
enum Tilstand {
  STARTET, IKKESTARTET, RAMT, SPILLER, AI, UDEFINERET, MARKOER
};

class Skib
{
  private float xAksen;
  private float yAksen;
  private float kant;
  private Tilstand tilstand;
  private Tilstand gh;
  private PImage piimg;

  public Skib()
  {
    this.xAksen = 0;
    this.yAksen = 0;
    this.tilstand = Tilstand.IKKESTARTET;
    gh = Tilstand.UDEFINERET;
    kant = 50;
    piimg = loadImage("mgs/ship.png");
  }

  public Skib(float xAksen, float yAksen)
  {
    this.xAksen = xAksen;
    this.yAksen = yAksen;
    this.tilstand = Tilstand.IKKESTARTET;
    gh = Tilstand.UDEFINERET;
    kant = 50;
    piimg = loadImage("img/ship.png");
  }

  public Tilstand tilstandNu()
  {
    return tilstand;
  }

  public Tilstand getGH()
  {
    return gh;
  }

  public void taendSpil(Tilstand h)
  {

    tilstand = Tilstand.STARTET;
    gh = h;
  }

  public void stopSpil(Tilstand h)
  {

    tilstand = Tilstand.IKKESTARTET;
    gh = h;
  }

  public void oedelagt()
  {
    tilstand = Tilstand.RAMT;
  }

  public void display()
  {
    final float KANT = 50;
    final float HALVKANT = KANT / 2.0f;
    if (tilstand == Tilstand.STARTET)
    {
      fill(0);
      stroke(105, 48, 10);
      rect(xAksen, yAksen, KANT, KANT);
      if (gh == Tilstand.SPILLER)
      {

        image(piimg, xAksen, yAksen);
      }
    } else if (tilstand == Tilstand.IKKESTARTET)
    {
      fill(0);
      stroke(105, 48, 10);
      rect(xAksen, yAksen, KANT, KANT);
    } else if (tilstand == Tilstand.RAMT)
    {
      fill(0);
      stroke(105, 48, 10);
      rect(xAksen, yAksen, KANT, KANT);
      if (gh == Tilstand.SPILLER)
      {
        stroke(0, 245, 0);
      } else if (gh == Tilstand.AI)
      {
        stroke(245, 0, 0);
      }
      line(xAksen, yAksen, xAksen+KANT, yAksen+KANT);
      line(xAksen, yAksen+KANT, xAksen+KANT, yAksen);
    } else if (tilstand == Tilstand.MARKOER)
    {
      if (gh == Tilstand.SPILLER)
      {
        fill(0, 245, 0);
      } else
      {
        fill(245, 0, 0);
      }
      stroke(105, 48, 10);
      rect(xAksen, yAksen, KANT, KANT);
    }
  }

  public boolean click()
  {
    if ((mouseX >= xAksen) && (mouseX <= xAksen + kant)
      && (mouseY >= yAksen) && (mouseY <= yAksen + kant))
    {
      return true;
    } else
    {
      return false;
    }
  }

  public void markoer(Tilstand h)
  {
    if (gh == Tilstand.UDEFINERET)
    {
      tilstand = Tilstand.MARKOER;
      gh = h;
    }
  }
}
class Plade
{
  private Skib [][] kort;
  private int A;
  private int spillerSkibe;
  private int aiSkibe;
  private final int MAXIMUMSKIBEPLACERET = 5;

  public Plade(int A)
  {
    this.A = A;
    spillerSkibe = 0;

    kort = new Skib[A][A];

    Skib skib = null;

    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {

        skib = new Skib(50*g, 50*h);
        kort[g][h] = skib;
      }
    }
  }

  public void display()
  {
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {
        kort[g][h].display();
      }
    }
  }


  public void placerSkibe()
  {

    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {
        if (kort[g][h].click())
        {
          if (kort[g][h].tilstandNu() == Tilstand.IKKESTARTET)
          {
            if (spillerSkibe < MAXIMUMSKIBEPLACERET)
            {
              kort[g][h].taendSpil(Tilstand.SPILLER);
              spillerSkibe++;
            }
          } else
          {
            kort[g][h].stopSpil(Tilstand.UDEFINERET);
            spillerSkibe--;
          }
        }
      }
    }
  }

  public void genererAiSkibe()
  {
    int skibCounter = 0;
    float randomValue = 0;
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {

        if ((skibCounter < MAXIMUMSKIBEPLACERET) && (kort[g][h].getGH() != Tilstand.AI) && (kort[g][h].getGH() != Tilstand.SPILLER))
        {
          randomValue = random(1);
          if (randomValue >= 0.7f && randomValue <= 0.9f )
          {
            if (kort[g][h].getGH() != Tilstand.SPILLER)
            {
              kort[g][h].taendSpil(Tilstand.AI);
              skibCounter++;
            }
          }
        }
      }
    }

    if (skibCounter == 5)
    {
      aiSkibe = 5;
    } else
    {
      throw new RuntimeException("...to little ships!");
    }
  }

  public void guess()
  {
    Skib skib = null;
    int optaeller = 0;
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {
        skib = kort[g][h];
        if (skib.click())
        {
          if (skib.getGH() == Tilstand.AI)
          {
            if (skib.tilstandNu() == Tilstand.STARTET)
            {

              skib.oedelagt();
              aiSkibe--;
            }
          } else
          {
            skib.markoer(Tilstand.SPILLER);
          }

          // pc guesses a ship.

          while (optaeller < 1)
          {
            skib = kort[(int)random(A)][(int)random(A)];
            while ((skib.getGH() == Tilstand.AI) || (skib.tilstandNu() == Tilstand.MARKOER)
              && (skib.tilstandNu() == Tilstand.RAMT))
            {
              skib = kort[(int)random(A)][(int)random(A)];
            }
            if (skib.getGH() == Tilstand.SPILLER)
            {
              if (skib.tilstandNu() == Tilstand.STARTET)
              {

                skib.oedelagt();
                optaeller++;
                spillerSkibe--;
              }
            } else
            {
              skib.markoer(Tilstand.AI);
              optaeller++;
            }
          }

          assert(optaeller == 1);
          optaeller = 0;
        }
      }
    }
  }

  public int getSpillerSkibe()
  {
    return spillerSkibe;
  }

  public int MAXSKIBE()
  {
    return MAXIMUMSKIBEPLACERET;
  }

  public Tilstand vundet()
  {
    if (aiSkibe == 0)
    {
      return Tilstand.SPILLER;
    }

    if (spillerSkibe == 0)
    {
      return Tilstand.AI;
    }

    return Tilstand.UDEFINERET;
  }


  public void genstart()
  {
    Skib skib = null;
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {

        skib = new Skib(50*g, 50*h);
        kort[g][h] = skib;
      }
    }

    spillerSkibe = aiSkibe = 0;
  }
}
  public void settings() {  size(500, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Saenkeslagskib" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
