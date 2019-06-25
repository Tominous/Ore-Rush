# Ore Rush
A Minecraft: Java Edition plugin created with the Spigot API to bring a unique event to your server.

### Users
**Installation**

To install you must have [MineResetLite](https://www.spigotmc.org/resources/mineresetlite-with-worldedit-v6-v7-tokenenchant-explosive-support.61713/) installed.

Once MineResetLite is installed you must:
1. Stop your server
2. Move Ore Rush.jar into your plugins folder
3. Start your server

**Commands and Permissions:**
 - /orerush
  > Description: View command usage in game
  
  > Permission: None
 - /orerush start [mine]
  > Description: Start an ore rush for a mine
  
  > Permission: orerush.start
  
  > Notes: If no mine is specified a random one will be chosen
 - /orerush end <mine>
  > Description: End an ore rush for a mine
  
  > Permission: orerush.end
 - /orerush endall
  > Description: End all active ore rushes
  
  > Permission: orerush.endall
  
### Developers
**API**

To access the OreRushAPI you must obtain an instance of the main class.

You can do this by using **OreRush oreRush = OreRush.getInstance();**

From there you can do **OreRushAPI oreRushAPI = oreRush.getOreRushAPI();**
