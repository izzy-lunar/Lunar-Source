def configure_quest_32():
    quest_id = 32
    quest_name = 'Brain Robbery'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_32(player):
    quest_stage = player.getQuest(32).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Brain Robbery", "Coming soon", " ", " ", " ")