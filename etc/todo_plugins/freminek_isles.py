def configure_quest_26():
    quest_id = 26
    quest_name = 'Fremennik Isles'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_26(player):
    quest_stage = player.getQuest(26).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Fremennik Isles", "Coming soon", " ", " ", " ")