package org.lazywizard.lazylib.campaign.dialog;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;

public class TestConvo
{
    static float relation = 0f;

    public static void main(String[] args)
    {
        ConversationMaster convo = new ConversationMaster();
        Node n1 = new Node("This is a test conversation:");
        Response r1 = new Response("Shouldn't appear", null, null,
                new Response.AppearsWhen()
        {
            @Override
            public boolean checkShouldAppear()
            {
                return false;
            }
        });
        Response r2 = new Response("Should appear", null, null,
                new Response.AppearsWhen()
        {
            @Override
            public boolean checkShouldAppear()
            {
                return true;
            }
        });
        Response r3 = new Response("Leads back to beginning", n1);
        Response r4 = new Response("Leads to beginning, plus shows message", n1,
                new Response.OnChosenScript()
        {
            @Override
            public void onChosen(CampaignFleetAPI fleet)
            {
                System.out.println("This response ran a script!");
            }
        }, null);
        Response r5 = new Response("(end conversation)", null);
        n1.addResponse(r1);
        n1.addResponse(r2);
        n1.addResponse(r3);
        n1.addResponse(r4);
        n1.addResponse(r5);

        final Node baseNode = new Node("Neutral: Greetings, can I help you?");
        Response insultResponse = new Response("<insult them>", baseNode,
                new Response.OnChosenScript()
        {
            @Override
            public void onChosen(CampaignFleetAPI fleet)
            {
                relation -= .1f;
                System.out.println("Relation dropped to " + relation + ".");
                if (relation == 0)
                {
                    baseNode.setText("Neutral: Greetings, can I help you?");
                }
                else if (relation < 0)
                {
                    baseNode.setText("Aggressive: State your business and leave.");
                }
            }
        }, null);
        Response attackResponse = new Response("<attack them>", null,
                new Response.OnChosenScript()
        {
            @Override
            public void onChosen(CampaignFleetAPI fleet)
            {
                System.out.println("Would attack now, ending conversation!");
            }
        },
                new Response.AppearsWhen()
        {
            @Override
            public boolean checkShouldAppear()
            {
                return (relation <= -.25f);
            }
        });
        Response complimentResponse = new Response("<compliment them>", baseNode,
                new Response.OnChosenScript()
        {
            @Override
            public void onChosen(CampaignFleetAPI fleet)
            {
                relation += .1f;
                System.out.println("Relation increased to " + relation + ".");
                if (relation == 0)
                {
                    baseNode.setText("Neutral: Greetings, can I help you?");
                }
                else if (relation > 0)
                {
                    baseNode.setText("Friendly: Greetings, friend! How may I help you?2");
                }
            }
        }, null);
        baseNode.addResponse(insultResponse);
        baseNode.addResponse(attackResponse);
        baseNode.addResponse(complimentResponse);

        convo.addConversation("test1", n1);
        convo.addConversation("test2", baseNode);
        convo.startConversation(new FakeFleet(), "test1");
        convo.startConversation(new FakeFleet(), "test2");
    }
}
