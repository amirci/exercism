import java.time.LocalDate
import monocle.syntax.all._

object LensPerson {
  case class Person(_name: Name, _born: Born, _address: Address)

  case class Name(_foreNames: String /*Space separated*/ , _surName: String)

  // Value of java.time.LocalDate.toEpochDay
  private type EpochDay = Long

  case class Born(_bornAt: Address, _bornOn: EpochDay)

  case class Address(_street: String, _houseNumber: Int,
    _place: String /*Village / city*/ , _country: String)

  // Valid values of Gregorian are those for which 'java.time.LocalDate.of'
  // returns a valid LocalDate.
  case class Gregorian(_year: Int, _month: Int, _dayOfMonth: Int)

  // Implement these.

  val bornStreet: Born => String = (born: Born) => born.focus(_._bornAt._street).get

  val setCurrentStreet: String => Person => Person = (street: String) => (person: Person) =>
    person.focus(_._address._street).set(street)

  val setBirthMonth: Int => Person => Person = month => person =>
    person.focus(_._born._bornOn).modify(LocalDate.ofEpochDay(_).withMonth(month).toEpochDay)

  // Transform both birth and current street names.
  val renameStreets: (String => String) => Person => Person = changeStreet => person =>
    person
      .focus(_._address._street).modify(changeStreet)
      .focus(_._born._bornAt._street).modify(changeStreet)
}
