package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.XTX;

@Relation(collectionRelation = "xtx")
public class XTXView implements View
{
    private final XTX xtx;

    public XTXView(XTX xtx)
    {
        this.xtx = xtx;
    }

    public Long getId() {
        return xtx.getId();
    }
}
