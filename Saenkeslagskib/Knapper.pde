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
