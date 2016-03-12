def configure_quest_32():
    quest_id = 32
    quest_name = 'Another Slice of H.A.M'
    quest_stages = 2
    World.addQuest(quest_id, quest_name, quest_stages)
    
def quest_button_32(player):
    quest_stage = player.getQuest(32).getStage()
    if quest_stage == 0: 
        player.getFunction().startInfo("Another Slice of H.A.M", "I can start this quest by speaking to @dre@Johanhus Ulsbrecht@bla@ who", "is located somewhere in Brimhaven Dungeon.", "", "")
    elif quest_stage == 1:
        player.boxMessage("I must bring 10 cut rubys, 20 iron bars and", "20 steel bars to @dre@Alrena@bla@ who is outside Ardougne church.")
    elif quest_stage == 2:
        player.boxMessage("I must bring 10 cut rubys, 20 iron bars and", "20 steel bars to @dre@Alrena@bla@ who is outside Ardougne church.")
 