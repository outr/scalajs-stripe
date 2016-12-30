package com.outr.stripe

import com.outr.scribe.Logging
import com.outr.stripe.support._

class Stripe(val apiKey: String) extends Restful with Implicits with Logging {
  private val baseURL = "https://api.stripe.com/v1"
  override protected def url(endPoint: String): String = s"$baseURL/$endPoint"

  lazy val balance: BalanceSupport = new BalanceSupport(this)
  lazy val charges: ChargesSupport = new ChargesSupport(this)
  lazy val customers: CustomersSupport = new CustomersSupport(this)
  lazy val disputes: DisputesSupport = new DisputesSupport(this)
  lazy val events: EventsSupport = new EventsSupport(this)
  lazy val refunds: RefundsSupport = new RefundsSupport(this)
  lazy val tokens: TokensSupport = new TokensSupport(this)
  lazy val transfers: TransfersSupport = new TransfersSupport(this)
  lazy val accounts: AccountsSupport = new AccountsSupport(this)
}

object Stripe {
  val Version = "2016-07-06"
}