import { MyComment } from '../types/Comment'
import { Component } from './Component'

export class CommentComponent extends Component {
  private comment: MyComment

  constructor(mountElement: HTMLElement, comment: MyComment) {
    super(mountElement)
    this.comment = comment
  }

  protected mounted(): void {
    // <div
    //         th:each="comment : ${comments}"
    //         class="row justify-content-center mb-3"
    // >
    //     <div class="col">

    //         <div class="card">
    //             <h4 th:text="${comment.username}" class="card-header">Username</h4>
    //             <div
    //                     class="card-body"
    //             >
    //                 <p th:text="${comment.text}">Text</p>
    //             </div>
    //         </div>
    //     </div>
    // </div>

    this.mountElement.innerHTML = ''

    const divRow = document.createElement('div')
    divRow.classList.add('row')
    divRow.classList.add('justify-content-center')
    divRow.classList.add('mt-3')

    const divCol = document.createElement('div')
    divCol.classList.add('col')

    const divCard = document.createElement('div')
    divCard.classList.add('card')

    const usernameElement = document.createElement('h4')
    usernameElement.classList.add('card-header')
    usernameElement.innerText = this.comment.username

    const cardBody = document.createElement('div')
    cardBody.classList.add('card-body')

    const commentTextElement = document.createElement('p')
    commentTextElement.innerText = this.comment.text

    cardBody.appendChild(commentTextElement)

    divCard.appendChild(usernameElement)
    divCard.appendChild(cardBody)
    divCol.appendChild(divCard)
    divRow.appendChild(divCol)
    this.mountElement.appendChild(divRow)
  }

  protected showState(): void {}
}
