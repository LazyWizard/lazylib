package org.lazywizard.lazylib.campaign.dialog;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;

public class Response
{
    private String text;
    private OnChosenScript onChosen;
    private AppearsWhen shouldAppear;
    private Node parent, leadsTo;

    public Response(String text, Node leadsTo)
    {
        this.text = text;
        this.leadsTo = leadsTo;
    }

    public Response(String text, Node leadsTo, OnChosenScript onChosen,
            AppearsWhen shouldAppear)
    {
        this(text, leadsTo);
        this.onChosen = onChosen;
        this.shouldAppear = shouldAppear;
    }

    public String getText()
    {
        return text;
    }

    protected void setParent(Node parent)
    {
        this.parent = parent;
    }

    public Node getParent()
    {
        return parent;
    }

    public void setShouldAppear(AppearsWhen criteria)
    {
        shouldAppear = criteria;
    }

    public boolean shouldAppear()
    {
        if (shouldAppear == null)
        {
            return true;
        }

        return shouldAppear.checkShouldAppear();
    }

    public void setOnChosenScript(OnChosenScript script)
    {
        onChosen = script;
    }

    public void setNodeLedTo(Node node)
    {
        leadsTo = node;
    }

    public void onChosen(CampaignFleetAPI talkingTo)
    {
        if (onChosen != null)
        {
            onChosen.onChosen(talkingTo);
        }

        if (leadsTo != null)
        {
            leadsTo.show(talkingTo);
        }
    }

    public interface OnChosenScript
    {
        public void onChosen(CampaignFleetAPI talkingTo);
    }

    public interface AppearsWhen
    {
        public boolean checkShouldAppear();
    }
}
