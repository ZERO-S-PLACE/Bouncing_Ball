My first bigger Java FX project. 

Almost whole mathematical background of application was coded from scratch by me.

Project consists of two major parts: 

Bouncy balls game

Game-like application which simulates movements and elastic collisions
of objects on 2D plane. Gravitational force and friction could be considered.
Collision points and velocities are calculated precisely (assuming that move of an object between frames
can be approximated as uniform rectilinear motion).

Goal of every level is to move all yellow objects to target area, without letting any 
red objects to enter it.

Level creator

Tool for creating game boards for part 1. Enables to create simple shapes, 
closed poly lines, and, also complex areas created by closed shapes boolean algebra-
they are not piles of overlapping shapes but calculated poly line vector boundaries of inner and outer areas.


It is first, test version of a project, and it is prepared for being extended by:

-adding different shapes of moving objects

-adding electromagnetic forces between objects

-adding leaderboards and user accounts

-more...
