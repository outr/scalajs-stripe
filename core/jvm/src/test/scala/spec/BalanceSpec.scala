package spec

import com.outr.stripe.{Money, QueryConfig}
import org.scalatest.{AsyncWordSpec, Matchers}

class BalanceSpec extends AsyncWordSpec with Matchers {
  "Balance" should {
    "list a test balance" in {
      TestStripe.balance().map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(balance) => {
          balance.`object` should be("balance")
          balance.available.length should be(6)
          balance.available.head.currency should be("usd")
          balance.available.head.amount should be(Money(227857805.68))
          balance.available.head.sourceTypes.card should be(Money(226757731.12))
          balance.livemode should be(false)
          balance.pending.length should be(6)
          balance.pending.last.currency should be("gbp")
        }
      }
    }
    "list most recent balance transaction history" in {
      TestStripe.balance.list(config = QueryConfig(limit = 1)).map {
        case Left(failure) => fail(s"Receive error response: ${failure.text} (${failure.code})")
        case Right(list) => {
          list.`object` should be("list")
          list.url should be("/v1/balance/history")
          list.hasMore should be(true)
          list.data.length should be(1)
        }
      }
    }
  }
}
