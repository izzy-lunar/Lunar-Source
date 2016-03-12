def configure_quest_34():
    quest_id = 34
    quest_name = 'Clock Tower'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_34(player):
    quest_stage = player.getQuest(34).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Death Plateu", "Coming soon", " ", " ", " ")