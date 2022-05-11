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
