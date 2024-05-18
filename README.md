# Entropic Block Magic

A mod that adds a bunch of utilities but its magic, honest.

there are 4 spells right now:

- __Sealing:__ Put slabs and stairs back together to get the original blocks
- __Deconstruction:__ unpolish, unbrick, unwall, untile blocks
- __Raising:__ extend structures (like a builders wand)
- __Exchange:__ replace blocks on the ground with the ones in your offhand
- __Stowing:__ throw all raw, cobble and ore items in your inventory on the ground in front of you
- __Searching:__ Scans all chests within a 10 block radius of you then you may search and request items

Each book can be crafted using a book and quill, a diamond, a prismarine shard
and another item (see your favourite item viewing mod I suppose)

## Oh yeah, the magic
you don't get these spells for free, they are powered by entropy

- every location in the world has an entropy score from -350 to 350
- each spell requires an entropy of a specific band (in bands of 100)
- the biome and your approximate y pos determine this value
- you can track the entropy of your current location with a biome charm
- you can also store the entropy of that location in the charm

## Loot
Additionally, each spell has a chance to be found in a loot chest and those versions are special

loot spells have alternative casting costs, and get a *special* name

## Compatibility?
Funnily enough, this mod should be very compatible from the get go.

Most of the utilities in this mod use Registry lookups rather than tags or hardcoded items;
this means that in 90% cases any mod that adds things that follow standard naming principles
(e.g someone could add a new polished stone) should automagically work with this mod.

