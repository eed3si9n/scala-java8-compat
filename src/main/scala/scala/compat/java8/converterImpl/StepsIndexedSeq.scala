package scala.compat.java8.converterImpl

import language.implicitConversions

import scala.compat.java8.collectionImpl._
import scala.compat.java8.runtime._

import Stepper._

/////////////////////////////
// Stepper implementations //
/////////////////////////////

private[java8] class StepsAnyIndexedSeq[A](underlying: collection.IndexedSeqLike[A, _], _i0: Int, _iN: Int)
extends StepsLikeIndexed[A, StepsAnyIndexedSeq[A]](_i0, _iN) {
  def next() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsAnyIndexedSeq[A](underlying, i0, half)
}

private[java8] class StepsDoubleIndexedSeq[CC <: collection.IndexedSeqLike[Double, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsDoubleLikeIndexed[StepsDoubleIndexedSeq[CC]](_i0, _iN) {
  def nextDouble() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsDoubleIndexedSeq[CC](underlying, i0, half)
}

private[java8] class StepsIntIndexedSeq[CC <: collection.IndexedSeqLike[Int, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsIntLikeIndexed[StepsIntIndexedSeq[CC]](_i0, _iN) {
  def nextInt() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsIntIndexedSeq[CC](underlying, i0, half)
}

private[java8] class StepsLongIndexedSeq[CC <: collection.IndexedSeqLike[Long, _]](underlying: CC, _i0: Int, _iN: Int)
extends StepsLongLikeIndexed[StepsLongIndexedSeq[CC]](_i0, _iN) {
  def nextLong() = if (hasNext()) { val j = i0; i0 += 1; underlying(j) } else throwNSEE
  def semiclone(half: Int) = new StepsLongIndexedSeq[CC](underlying, i0, half)
}

//////////////////////////
// Value class adapters //
//////////////////////////

final class RichIndexedSeqCanStep[T](private val underlying: collection.IndexedSeqLike[T, _]) extends AnyVal with MakesParStepper[T] {
  override def stepper[S <: Stepper[_]](implicit ss: StepperShape[T, S]) = (ss match {
    case ss if ss.ref             => new StepsAnyIndexedSeq[T](underlying,                                                    0, underlying.length)
    case StepperShape.IntValue    => new StepsIntIndexedSeq   (underlying.asInstanceOf[collection.IndexedSeqLike[Int, _]],    0, underlying.length)
    case StepperShape.LongValue   => new StepsLongIndexedSeq  (underlying.asInstanceOf[collection.IndexedSeqLike[Long, _]],   0, underlying.length)
    case StepperShape.DoubleValue => new StepsDoubleIndexedSeq(underlying.asInstanceOf[collection.IndexedSeqLike[Double, _]], 0, underlying.length)
    case ss                       => super.stepper(ss)
  }).asInstanceOf[S with EfficientSubstep]
}
