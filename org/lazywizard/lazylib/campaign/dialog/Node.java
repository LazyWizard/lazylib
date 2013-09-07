package org.lazywizard.lazylib.campaign.dialog;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Node
{
    private String text = "<no text entered>";
    private List<Response> responses = new ArrayList();

    public Node()
    {
    }

    public Node(String text)
    {
        this.text = text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void addResponse(Response response)
    {
        responses.add(response);
        response.setParent(this);
    }

    public String getText()
    {
        return text;
    }

    public void show(CampaignFleetAPI fleet)
    {
        List<Response> visibleResponses = new ArrayList();
        Response tmp;

        for (Iterator<Response> iter = responses.iterator(); iter.hasNext();)
        {
            tmp = iter.next();
            if (tmp.shouldAppear())
            {
                visibleResponses.add(tmp);
            }
        }

        System.out.println(this.text);
        for (int x = 0; x < visibleResponses.size(); x++)
        {
            tmp = visibleResponses.get(x);
            System.out.println("   " + (x + 1) + ") " + tmp.getText());
        }

        Scanner input = new Scanner(System.in);
        int choice = input.nextInt() - 1;
        while (choice < 0 || choice >= visibleResponses.size())
        {
            System.out.println("Invalid choice, try again!");
            choice = input.nextInt() - 1;
        }
        tmp = visibleResponses.get(choice);
        tmp.onChosen(fleet);
    }
}
