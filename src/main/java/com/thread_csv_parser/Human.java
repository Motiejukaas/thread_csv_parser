package com.thread_csv_parser;

public class Human {
    private String first_name;
    private String last_name;
    private String email;
    private String image_link;
    private String ip_address;
    private String filename;

    public Human(String first_name, String last_name, String email, String image_link, String ip_address) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.image_link = image_link;
        this.ip_address = ip_address;
        generateFilename();
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void generateFilename() {
        String first_name_filename = first_name.substring(0, Math.min(first_name.length(), 3));
        String last_name_filename = last_name.substring(0, Math.min(last_name.length(), 3));
        String last_numbers = ip_address.substring(ip_address.lastIndexOf('.') + 1);

        this.filename = first_name_filename + last_name_filename + last_numbers;
    }
}
