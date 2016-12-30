package com.outr.stripe.support

import com.outr.stripe.charge.Card
import com.outr.stripe.{Deleted, Implicits, Pickler, QueryConfig, Stripe, StripeList}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomerCreditCardsSupport(stripe: Stripe) extends Implicits {
  def create(customerId: String,
             source: Option[String] = None,
             externalAccount: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             metadata: Map[String, String] = Map.empty): Future[Card] = {
    val data = List(
      source.map("source" -> _),
      externalAccount.map("external_account" -> _),
      defaultForCurrency.map("default_for_currency" -> _),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None
    ).flatten
    stripe.post(s"customers/$customerId/sources", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Card](response.body)
    }
  }

  def byId(customerId: String, cardId: String): Future[Card] = {
    stripe.get(s"customers/$customerId/sources/$cardId", QueryConfig.default).map { response =>
      Pickler.read[Card](response.body)
    }
  }

  def update(customerId: String,
             cardId: String,
             addressCity: Option[String] = None,
             addressCountry: Option[String] = None,
             addressLine1: Option[String] = None,
             addressLine2: Option[String] = None,
             addressState: Option[String] = None,
             addressZip: Option[String] = None,
             defaultForCurrency: Option[String] = None,
             expMonth: Option[Int] = None,
             expYear: Option[Int] = None,
             metadata: Map[String, String] = Map.empty,
             name: Option[String] = None): Future[Card] = {
    val data = List(
      addressCity.map("address_city" -> _),
      addressCountry.map("address_country" -> _),
      addressLine1.map("address_line1" -> _),
      addressLine2.map("address_line2" -> _),
      addressState.map("address_state" -> _),
      addressZip.map("address_zip" -> _),
      defaultForCurrency.map("default_for_currency" -> _),
      expMonth.map("exp_month" -> _.toString),
      expYear.map("exp_year" -> _.toString),
      if (metadata.nonEmpty) Some("metadata" -> Pickler.write(metadata)) else None,
      name.map("name" -> _)
    ).flatten
    stripe.post(s"customers/$customerId/sources/$cardId", QueryConfig.default, data: _*).map { response =>
      Pickler.read[Card](response.body)
    }
  }

  def delete(customerId: String, cardId: String): Future[Deleted] = {
    stripe.delete(s"customers/$customerId/sources/$cardId", QueryConfig.default).map { response =>
      Pickler.read[Deleted](response.body)
    }
  }

  def list(customerId: String, config: QueryConfig = QueryConfig.default): Future[StripeList[Card]] = {
    stripe.get(s"customers/$customerId/sources", config, "object" -> "card").map { response =>
      Pickler.read[StripeList[Card]](response.body)
    }
  }
}
