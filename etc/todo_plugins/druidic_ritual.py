def configure_quest_29():
    quest_id = 29
    quest_name = 'Druidic Ritual'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_29(player):
    quest_stage = player.getQuest(29).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Druidic Ritual", "Coming soon", " ", " ", " ")