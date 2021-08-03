package com.anggaari.converter.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "task")
public class Task {
    @XmlElement(name = "id")
    public long id;

    @XmlElement(name = "title")
    public String title;

    @XmlElement(name = "description")
    public String description;

    @XmlAttribute(required = false)
    public String link;

    public Task() {}
}
