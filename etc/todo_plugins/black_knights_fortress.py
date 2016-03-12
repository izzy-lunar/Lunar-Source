def configure_quest_28():
    quest_id = 28
    quest_name = 'Black Knights Fortress'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_28(player):
    quest_stage = player.getQuest(28).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Black Knights Fortress", "Coming soon", " ", " ", " ")