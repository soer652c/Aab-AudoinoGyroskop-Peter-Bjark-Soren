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

  public void stopSpil(Tilstand h) {
    tilstand = Tilstand.IKKESTARTET;
    gh = h;
  }

  public void ramt() {
    tilstand = Tilstand.RAMT;
  }

  public void display() {
    final float KANT = 50;
    final float HALVKANT = KANT * 0.5;
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

  }

  public boolean mouseClicked() {
    if((mouseY >= yAksen) && (mouseY <= yAksen + kant) && (mouseX >= xAksen) && (mouseX <= xAksen + kant)) {
      return true;
    }
    else {
      return false;
    }
  }

  public void markoer(Tilstand h) {
    if(gh == Tilstand.UDEFINERET) {
      tilstand = Tilstand.MARKOER;
      gh = h;
    }
  }
