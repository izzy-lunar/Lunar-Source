##################################
###       Author: Robbie       ###
###        Date:4.6.2015       ###
###     Bounty Hunter Script   ###
##################################

from com.ownxile.core import World
from com.ownxile.rs2.player import PlayerHandler

def award_kill(player, victim):
    if player.getId() == victim.getId():
        target_name = PlayerHandler.players[player.targetId].playerName

def assign_target(player):
    target_id = World.getBountyHunter().getPlayer(get_target_id(player))
	
def get_target_id(player):
    player = 0
	for player in World.getBountyHunter().getPlayers().length():
        player = player + 1
    return player
	
def 