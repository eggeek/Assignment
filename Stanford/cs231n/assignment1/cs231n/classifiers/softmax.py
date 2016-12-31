import numpy as np
from random import shuffle

def softmax_loss_naive(W, X, y, reg):
  """
  Softmax loss function, naive implementation (with loops)

  Inputs have dimension D, there are C classes, and we operate on minibatches
  of N examples.

  Inputs:
  - W: A numpy array of shape (D, C) containing weights.
  - X: A numpy array of shape (N, D) containing a minibatch of data.
  - y: A numpy array of shape (N,) containing training labels; y[i] = c means
    that X[i] has label c, where 0 <= c < C.
  - reg: (float) regularization strength

  Returns a tuple of:
  - loss as single float
  - gradient with respect to weights W; an array of same shape as W
  """
  # Initialize the loss and gradient to zero.
  loss = 0.0
  dW = np.zeros_like(W)
  N = X.shape[0]
  C = W.shape[1]

  f = X.dot(W)
  # r: (N, 1)
  r = np.sum(np.power(np.e, f), axis=1)
  #############################################################################
  # TODO: Compute the softmax loss and its gradient using explicit loops.     #
  # Store the loss in loss and the gradient in dW. If you are not careful     #
  # here, it is easy to run into numeric instability. Don't forget the        #
  # regularization!                                                           #
  #############################################################################
  for i in xrange(N):
    Li = -f[i, y[i]] + np.log(np.sum(np.power(np.e, f[i, :])))
    loss += Li
    dW[:, y[i]] -= X[i]
  for i in xrange(C):
    for k in xrange(N):
      dW[:, i] += np.power(np.e, f[k, i]) / r[k] * X[k]
  #############################################################################
  #                          END OF YOUR CODE                                 #
  #############################################################################
  loss /= N
  loss += 0.5 * reg * np.sum(W * W)
  dW /= N
  dW += reg * W
  return loss, dW


def softmax_loss_vectorized(W, X, y, reg):
  """
  Softmax loss function, vectorized version.

  Inputs and outputs are the same as softmax_loss_naive.
  """
  # Initialize the loss and gradient to zero.
  loss = 0.0
  dW = np.zeros_like(W)

  #############################################################################
  # TODO: Compute the softmax loss and its gradient using no explicit loops.  #
  # Store the loss in loss and the gradient in dW. If you are not careful     #
  # here, it is easy to run into numeric instability. Don't forget the        #
  # regularization!                                                           #
  #############################################################################
  N = X.shape[0]
  C = W.shape[1]
  f = X.dot(W)
  epf = np.power(np.e, f)

  # loss calculation
  # loss = -np.sum(f[np.arange(N), y]) + np.log(np.sum(epf))
  loss = -np.sum(f[np.arange(N), y]) + np.sum(np.log(np.sum(epf, axis=1)))
  loss /= N
  loss += 0.5 * reg * np.sum(W ** 2)

  # dW calculation
  t = np.zeros((N, C))
  t[np.arange(N), y] = 1
  # t.T.dot(X) is sum of X, group by classes
  dW -= t.T.dot(X).T
  # dW: sum of log
  r = np.sum(epf, axis=1)
  t = np.divide(epf, r.reshape(N, 1))
  dW += X.T.dot(t)
  dW /= N
  dW += reg * W
  #############################################################################
  #                          END OF YOUR CODE                                 #
  #############################################################################

  return loss, dW

