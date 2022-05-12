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
    final float HALVKANT = KANT / 2.0;
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
