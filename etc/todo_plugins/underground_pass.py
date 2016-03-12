def configure_quest_31():
    quest_id = 31
    quest_name = 'Underground Pass'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_31(player):
    quest_stage = player.getQuest(31).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Underground Pass", "Coming soon", " ", " ", " ")