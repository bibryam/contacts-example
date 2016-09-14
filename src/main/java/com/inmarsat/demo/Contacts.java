package com.inmarsat.demo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "contacts")
@XmlAccessorType(XmlAccessType.FIELD)
public class Contacts {

    @XmlElement(name = "contact")
    private List<Contact> contacts;

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "contacts=" + contacts +
                '}';
    }
}
