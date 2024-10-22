import { createTypes, toValidableType } from "vue-types"
const newPropTypes = createTypes({
  func: undefined,
  bool: undefined,
  string: undefined,
  number: undefined,
  object: undefined,
  integer: undefined
})

class propTypes extends newPropTypes {
  static get style() {
    return toValidableType("style", {
      type: [String, Object]
    })
  }
}

export { propTypes }
