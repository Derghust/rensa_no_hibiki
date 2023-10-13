package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.predicates.all.{Greater, Less, Not}

type Scroll = Not[Less[0.0]] & Not[Greater[1.0]]

case class Progress(
    scroll: Scroll
)
