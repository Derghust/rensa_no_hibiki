package com.github.derghust.pekkotemplate.structure

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.string.IPv4
import eu.timepit.refined.string.IPv6
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.numeric.*

type Scroll       = Not[Less[0.0]] & Not[Greater[1.0]]
type Page         = Positive
type IPAdress     = String Refined IPv4 Or IPv6
type TrackedManga = Map[Uuid, Progress]

final case class User(
    id: Uuid,
    ipAdresses: Vector[IPAdress],
    trackedManga: TrackedManga,
)

final case class UserMetric(
    id: Uuid,
    averageAPICalls: Counter,
    currentAPICalls: Counter,
    averagePage: Counter,
    currentPage: Counter,
)

final case class Progress(
    page: Page,
    scroll: Scroll,
)
