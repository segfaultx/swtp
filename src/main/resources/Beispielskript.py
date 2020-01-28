# Das zu bearbeitende Objekt (die Liste von Tauschangeboten) ist unter dem namen 'offers' ansprechbar
# z.B. um ein Element der Liste abzurufen -> offers[0] | offers.remove(1), etc.
# Die Liste kann mit normalen Python operationen wie remove, get, append, pop etc. veraendert werden
#
# Objekte und deren Felder in der Liste koennen ganz normal via Objekt.Feldname angesprochen/verwendet werden
#
# Die Elemente der Liste enthalten die Folgenden Objekte:
# TradeOffer mit den Feldern: id, seeker (vom typ User), offer (vom typ Timeslot), seek (vom typ Timeslot), instantTrade (boolean)
# User mit den Feldern: id, email, firstName, lastName, studentNumber, cp, fairness, po, waitLists, modules, tradeoffers, completedModules und timeslots
# Timeslot mit den Feldern: id, user (Lehrkraft), room (vom typ Room), day, timeStart, timeEnd, module, attendees (eine Liste von Studenten vom typ User), waitList (wie attendees)
# Timeslot.room: id, roomNumber (String), location (String)
#
# Eine Moeglichkeit waere also die Tauschangebote nach den Raeumen der gesuchten Timeslots zu filtern

# Beispiel
for tradeoffer in offers:
    timeslot_seek = tradeoffer.seek
    if timeslot_seek.room.roomNumber == "D12":
        offers.remove(tradeoffer)