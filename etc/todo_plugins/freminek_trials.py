def configure_quest_25():
    quest_id = 25
    quest_name = 'Fremennik Trials'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_25(player):
    quest_stage = player.getQuest(25).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Fremennik Trials", "Coming soon", " ", " ", " ")