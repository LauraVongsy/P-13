// src/app/chat/chat.component.ts
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatService } from '../service/chat.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrls: ['./chat.component.scss'],
  templateUrl: './chat.component.html'
})
export class ChatComponent implements OnInit {
  messages: { author: string; body: string }[] = [];
  newMessage = '';
  currentUser: 'user1' | 'user2' = 'user1';

  constructor(private chatService: ChatService) {}

  async ngOnInit() {
    await this.setupChat();
  }

  async setupChat() {
    this.messages = []; // Clear messages on user change
    await this.chatService.initClient(this.currentUser);

    const {messages } = await this.chatService.getOrCreateConversation('poc-room');

    this.messages.push(...messages);

    this.chatService.onMessageAdded((msg) => {
      this.messages.push(msg);
    });
  }


  async selectUser() {
    // Disconnect the current client user
    await this.chatService.disconnect()
    // Re-run the setup logic for the new user
    await this.setupChat();
  }
  
  async sendMessage() {
    if (!this.newMessage.trim()) return;
    await this.chatService.sendMessage(this.newMessage);
    this.newMessage = '';
  }
}