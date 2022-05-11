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

public class Battleship2 extends PApplet {

Plade plade;
Knapper start;
Knapper exit;
Knapper spil_ovre;

enum Flag {
  STANDBY, NYTSPIL, EXITSPIL, POSITION, GAET
};

// enumeration type for variable error
enum Error {
  VARIABLEERROR, OK
};

// flag variables for control flag.
Flag flag;
Flag flag2;

// for error handling
Error error;

Tilstand vundet;

public void setup() {
  
  background(245);
  frameRate(30);

  flag = Flag.STANDBY;
  flag2 = Flag.POSITION;

  error = Error.OK;

  vundet = Tilstand.UDEFINERET;

  plade = new Plade(10);
  start = new Knapper(150.00f, 150.00f, "START");
  exit = new Knapper(150.00f, 214.00f, "Exit");
  spil_ovre = new Knapper(0.00f, 500.00f, "Done");
}

public void draw() {
  background(255);

  // display little menu
  if (flag == Flag.STANDBY) {

    start.display();
    exit.display();
    switch(vundet) {
    case SPILLER:
      fill(0);
      textSize(50);
      text("You vundet!", 150, 150);
      break;
    case AI:
      fill(0);
      textSize(50);
      text("You lose!", 150, 150);
      break;
    }
  }
  else if (flag == Flag.NYTSPIL) {
    plade.display();
    spil_ovre.oplaast();
    vundet = Tilstand.UDEFINERET;
    if (flag2 == Flag.POSITION)
    {
      spil_ovre.display();
    }
  }
  else
  {
    fill(0);
    textSize(50);
    text("SLUT", 150, 150);
  }

  switch (error)
  {
  case VARIABLEERROR:
    fill(0);
    textSize(20);
    text("Ikke nok skibe placeret", 260, 550);
    break;
  }
}

public void mouseClicked()
{
  if(mouseButton == LEFT)
  {
    if(start.click())
    {
      start.displayEt(); // for knap animation.
      flag = Flag.NYTSPIL;
      start.laast();
      exit.laast();
    }

    if (exit.click())
    {
      exit.displayEt(); // for knap animation.
      flag = Flag.EXITSPIL;
      start.laast();
      exit.laast();
    }

    if (flag == Flag.NYTSPIL)
    {
      if (flag2 == Flag.POSITION)
      {
        plade.placerSkibe();
      } else if (flag2 == Flag.GAET)
      {
        plade.gaet();
        switch(plade.vundet())
        {
        case SPILLER:
          flag = Flag.STANDBY;
          flag2 = Flag.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.SPILLER;
          plade.genstart();
          break;
        case AI:
          flag = Flag.STANDBY;
          flag2 = Flag.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.AI;
          plade.genstart();
          break;
        }
      }

      if (spil_ovre.click())
      {
        spil_ovre.displayEt(); // for knap animation.
        if (plade.getSpillerSkibe() == plade.MAXSKIBE())
        {
          spil_ovre.laast();
          flag2 = Flag.GAET;
          error = Error.OK;
          plade.aiSkibe();
        }
        else
        {
          error = Error.VARIABLEERROR;
        }
      }
    }
  }
}
class Knapper {
  private float yAksen;
  private float xAksen;
  private float brede;
  private float hoejde;
  private boolean tilstand;
  private int indeks;
  private String navn;
  private PImage [] piimgs;
  private PImage knapbilleder;

  public Knapper(float yAksen, float xAksen, String navn) {
    this.yAksen = yAksen;
    this.xAksen = xAksen;
    this.navn = navn;
    brede = 300;
    hoejde = 75;
    tilstand = true;

    indeks = 0;
    knapbilleder = loadImage("knap.png");
    piimgs = new PImage[2];
    piimgs[0] = knapbilleder.get(0, 64, 256, 64);
    piimgs[1] = knapbilleder.get(0, 0, 256, 64);
  }

  public void display() {
    final float FORSKYDNING = 32;
    image(piimgs[0], xAksen, yAksen);
    fill(0);
    textSize(30);
    text(navn, yAksen+FORSKYDNING, xAksen+FORSKYDNING);
  }

  public void displayNull() {
    final float FORSKYDNING = 32;
    image(piimgs[0], xAksen, yAksen);
    fill(0);
    textSize(30);
    text(navn, yAksen+FORSKYDNING, xAksen+FORSKYDNING);
  }

  public void displayEt() {
    final float FORSKYDNING = 32;
    image(piimgs[1], xAksen, yAksen);
    fill(0);
    textSize(30);
    text(navn, yAksen+FORSKYDNING, xAksen+FORSKYDNING);
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
class Plade {
  private int A; //Navn på int
  private int spillerSkibe; //Spillerens Skibe
  private int aiSkibe; //Computerens Skibe
  private final int MAXIMUMSKIBEPLACERET = 5; //Den maksimale mængde skibe spilleren kan placere
  private Skib [][] kort;

  public Plade(int A)
  {
    this.A = A;
    spillerSkibe = 0;
    kort = new Skib[A][A];
    Skib skib = null;

    for(int g = 0; g < A; g++)
    {
      for(int h = 0; h < A; h++)
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
    for(int g = 0; g < A; g++)
    {
      for(int h = 0; h < A; h++)
      {
        if (kort[g][h].click())
        {
          if (kort[g][h].tilstandNu() == Tilstand.IKKESTARTET)
          {
            if (spillerSkibe < MAXIMUMSKIBEPLACERET) {
              kort[g][h].taendSpil(Tilstand.SPILLER);
              spillerSkibe++;
            }
          }
        else
          {
          kort[g][h].stopSpil(Tilstand.UDEFINERET);
          spillerSkibe--;
          }
        }
      }
    }
  }

  public void aiSkibe() {
    int skibCounter = 0;
    float randomValue = 0;
    for (int g = 0; g < A; g++) {
      for (int h = 0; h < A; h++) {
        if ((skibCounter < MAXIMUMSKIBEPLACERET) && (kort[g][h].getGH() != Tilstand.AI) && (kort[g][h].getGH() != Tilstand.SPILLER)) {
          randomValue = random(1);
          if (randomValue >= 0.7f && randomValue <= 0.9f) {
            if (kort[g][h].getGH() != Tilstand.SPILLER) {
              kort[g][h].taendSpil(Tilstand.AI);
              skibCounter++;
            }
          }
        }
      }
    }
  if (skibCounter == 5) {
    aiSkibe = 5;
  }
  else {
    throw new RuntimeException("placer flere skibe");
  }
  }

  public void gaet() {
    Skib skib = null;
    int optaeller = 0;
    for (int g = 0; g < A; g++) {
      for (int h = 0; h < A; h++) {
        skib = kort[g][h];
        if (skib.click()) {
          if (skib.getGH() == Tilstand.AI) {
            if (skib.tilstandNu() == Tilstand.STARTET) {
              skib.markoer();
              aiSkibe--;
              }
            }
            else {
              skib.markoer(Tilstand.SPILLER);
            }
            while (optaeller < 1)
            {
              skib = kort[(int)random(A)][(int)random(A)];
              while ((skib.getGH() == Tilstand.AI) || (skib.tilstandNu() == Tilstand.MARKOER) && (skib.tilstandNu() == Tilstand.RAMT))
              {
                skib = kort[(int)random(A)][(int)random(A)];
              }
              if (skib.getGH() == Tilstand.SPILLER)
              {
                if (skib.tilstandNu() == Tilstand.STARTET)
                {
                  skib.markoer();
                  optaeller++;
                  spillerSkibe--;
                }
               else
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
  }
  public int getSpillerSkibe() {
      return spillerSkibe;
  }

  public int MAXSKIBE() {
    return MAXIMUMSKIBEPLACERET;
  }

  public Tilstand vundet() {
    if (aiSkibe == 0){
      return Tilstand.SPILLER;
    }
    if (spillerSkibe == 0) {
      return Tilstand.AI;
    }
    return Tilstand.UDEFINERET;
  }

  public void genstart() {
    Skib skib = null;
    for (int g = 0; g < A; g++) {
      for (int h = 0; h < A; h++) {
        skib = new Skib(50*g, 50*h);
        kort[g][h] = skib;
      }
    }
    spillerSkibe = aiSkibe = 0;
  }

}
enum Tilstand {
  STARTET, IKKESTARTET, RAMT, SPILLER, AI, UDEFINERET, MARKOER
};

class Skib {
  private Tilstand gh;
  private Tilstand tilstand;
  private float kant;
  private float yAksen;
  private float xAksen;
  private PImage piimg;

  public Skib() {
    this.yAksen = 0;
    this.xAksen = 0;
    this.tilstand = Tilstand.IKKESTARTET;
    gh = Tilstand.UDEFINERET;
    kant = 50;
    piimg = loadImage("krigsskib.png");
  }

  public Skib(float yAksen, float xAksen) {
    this.yAksen = yAksen;
    this.xAksen = xAksen;
    this.tilstand = Tilstand.IKKESTARTET;
    gh = Tilstand.UDEFINERET;
    kant = 50;
    piimg = loadImage("krigsskib.png");
  }

  public Tilstand tilstandNu() {
    return tilstand;
  }

  public Tilstand getGH() {
    return gh;
  }

  public void taendSpil(Tilstand h){
  tilstand = Tilstand.STARTET;
  gh = h;
  }

  public void stopSpil(Tilstand h) {
    tilstand = Tilstand.IKKESTARTET;
    gh = h;
  }

  public void markoer() {
    tilstand = Tilstand.RAMT;
  }

  public void display() {
    final float KANT = 50;
    final float HALVKANT = KANT * 0.5f;
    if(tilstand == Tilstand.STARTET) {
      fill(245, 0, 0);
      stroke(195, 224, 58);
      rect(yAksen, xAksen, KANT, KANT);
      if(gh == tilstand.SPILLER) {
        image(piimg, xAksen, yAksen);
      }
      else if(tilstand == Tilstand.IKKESTARTET) {
        fill(245, 0, 0);
        stroke(195, 224, 58);
        rect(yAksen, xAksen, KANT, KANT);
      }
      else if(tilstand == Tilstand.RAMT); {
        fill(245, 0, 0);
        stroke(195, 224, 58);
        rect(yAksen, xAksen, KANT, KANT);
        if(gh == Tilstand.SPILLER) {
            stroke(195, 224, 58);
          }
          else if(gh == Tilstand.AI) {
            stroke(195, 224, 58);
          }
          line(xAksen, yAksen, xAksen+KANT, yAksen+KANT);
          line(xAksen, yAksen+KANT, xAksen+KANT, yAksen);
        }
      }
      else if(tilstand == Tilstand.MARKOER) {
        if(gh == Tilstand.SPILLER) {
          fill(245, 0, 0);
        }
        else {
          fill(0, 245, 0);
        }
        stroke(195, 224, 58);
        rect(yAksen, xAksen, KANT, KANT);
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

  public void markoer(Tilstand h) {
    if(gh == Tilstand.UDEFINERET) {
      tilstand = Tilstand.MARKOER;
      gh = h;
    }
  }
}
  public void settings() {  size(600, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Battleship2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
