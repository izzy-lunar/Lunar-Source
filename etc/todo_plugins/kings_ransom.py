def configure_quest_30():
    quest_id = 30
    quest_name = 'Kings Ransom'
    quest_stages = 5
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_30(player):
    quest_stage = player.getQuest(30).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Kings Ransom", "Coming soon", " ", " ", " ")