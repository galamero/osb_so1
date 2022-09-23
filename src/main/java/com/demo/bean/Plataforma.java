package com.demo.bean;

public class Plataforma {
    private int idPlatformOrigin;
    private int idCoin;
    private int idPlatformDestiny;
    private int idProduct;
    private int mount;
    private int quantity;

    public Plataforma() {

    }
    public Plataforma(
            int idPlatformOrigin,
            int idCoin,
            int idPlatformDestiny,
            int idProduct,
            int mount,
            int quantity) {
        this.idPlatformOrigin = idPlatformOrigin;
        this.idCoin = idCoin;
        this.idPlatformDestiny = idPlatformDestiny;
        this.idProduct = idProduct;
        this.mount = mount;
        this.quantity = quantity;
    }


    public int getIdPlatformOrigin() {
        return idPlatformOrigin;
    }

    public void setIdPlatformOrigin(int idPlatformOrigin) {
        this.idPlatformOrigin = idPlatformOrigin;
    }

    public int getIdCoin() {
        return idCoin;
    }

    public void setIdCoin(int idCoin) {
        this.idCoin = idCoin;
    }

    public int getIdPlatformDestiny() {
        return idPlatformDestiny;
    }

    public void setIdPlatformDestiny(int idPlatformDestiny) {
        this.idPlatformDestiny = idPlatformDestiny;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
