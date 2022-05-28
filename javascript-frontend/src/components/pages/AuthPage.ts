import { AuthStatus } from '../../types/AuthStatus'
import { doRequest } from '../../utils/Request'
import { eventBus } from '../EventBus'
import { ToastMessage } from '../ToastComponent'
import { Page } from './Page'

type LoginFormElements = {
  username: { value: string }
  password: { value: string }
}

type RegistrationFormElements = LoginFormElements & {
  email: { value: string }
  passwordconfirm: { value: string }
}

export class AuthPage extends Page {
  protected mounted(): void {
    this.mountElement.innerHTML = `        
        <div class="row justify-content-center gy-4">
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card">
                    <h3 class="card-header">Login</h3>
                    <div class="card-body">
                        <form id="loginForm">
                            <fieldset>
                                <div class="form-group">
                                    <label for="usernamelogin" class="form-label mt-4">Username</label>
                                    <input type="text" class="form-control" aria-describedby="username"
                                           id="usernamelogin"
                                           placeholder="Enter username" name="username" required>
        
                                    <label for="passwordlogin" class="form-label mt-4">Password</label>
                                    <input type="password" class="form-control" id="passwordlogin" placeholder="Password"
                                           name="password" required>
                                </div>
        
                                <div class="d-grid gap-2 mt-4">
                                    <button class="btn btn-primary" id="loginButton">Login</button>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card">
                    <h3 class="card-header">Registration</h3>
                    <div class="card-body">
                        <form id="registrationForm">
                            <fieldset>
                                <div class="form-group">
                                    <label for="emailregistration" class="form-label mt-4">Email address</label>
                                    <input type="email" class="form-control" aria-describedby="emailHelp"
                                           id="emailregistration"
                                           placeholder="Enter email" name="email" required>
        
                                    <label for="usernameregistration" class="form-label mt-4">Username</label>
                                    <input type="text" class="form-control" id="usernameregistration"
                                           aria-describedby="username"
                                           placeholder="Enter username" name="username" required>
        
                                    <label for="passwordregistration" class="form-label mt-4">Password</label>
                                    <input type="password" class="form-control" id="passwordregistration"
                                           placeholder="Password"
                                           name="password" required>
        
                                    <label for="passwordregistrationconfirm" class="form-label mt-4">Confirm
                                        Password</label>
                                    <input type="password" class="form-control" id="passwordregistrationconfirm"
                                           placeholder="Confirm Password"
                                           name="passwordconfirm" required>
                                </div>
        
        
                                <div class="d-grid gap-2 mt-4">
                                    <button class="btn btn-primary" id="registrationButton">Register</button>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        `
    const loginForm = document.getElementById('loginForm')!

    loginForm.addEventListener('submit', (event) => {
      event.preventDefault()
      event.stopPropagation()

      const elements = (event.target as HTMLFormElement)
        .elements as unknown as LoginFormElements

      this.tryLogin(elements.username.value, elements.password.value)
    })

    const registrationForm = document.getElementById('registrationForm')!

    registrationForm.addEventListener('submit', (event) => {
      event.preventDefault()
      event.stopPropagation()

      const elements = (event.target as HTMLFormElement)
        .elements as unknown as RegistrationFormElements

      let checkEmailRegex = /^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-.]+\.+[a-zA-Z0-9]+$/

      const email = elements.email.value

      if (!email.match(checkEmailRegex)) {
        eventBus.notifySubscribers('toast', {
          isError: true,
          message: 'Invalid Email Format',
        } as ToastMessage)
        return
      }

      const password = elements.password.value
      const passwordconfirm = elements.passwordconfirm.value

      if (password !== passwordconfirm) {
        eventBus.notifySubscribers('toast', {
          isError: true,
          message: 'Password and Confirm Password does not match',
        } as ToastMessage)
        return
      }

      this.tryRegistration(
        email,
        elements.username.value,
        password,
        passwordconfirm
      )
    })
  }

  private async tryRegistration(
    email: string,
    username: string,
    password: string,
    passwordconfirm: string
  ) {
    try {
      await doRequest('/register', 'POST', {
        email: email,
        username: username,
        password: password,
        passwordconfirm: passwordconfirm,
      })
      eventBus.notifySubscribers('logged', {
        isLogged: true,
      } as AuthStatus)
    } catch (error) {
      eventBus.notifySubscribers('toast', {
        isError: true,
        message: error,
      } as ToastMessage)
    }
  }

  private async tryLogin(username: string, password: string) {
    try {
      await doRequest('/login', 'POST', {
        username: username,
        password: password,
      })
      eventBus.notifySubscribers('logged', {
        isLogged: true,
      } as AuthStatus)
    } catch (error) {
      eventBus.notifySubscribers('toast', {
        isError: true,
        message: error,
      } as ToastMessage)
    }
  }

  protected showState(): void {}
}
