<template>
 <div class="hello">
    <h1>{{ msg }}</h1>
    
    <button class="Search__button" @click="callRestService()">Call Spring Boot REST backend</button>
    <button class="Search_button_2" @click="callGetService()">Call Spring Boot G backend</button>
    <div id="v-model-select-dynamic" class="demo">
  <select v-model="selected">
    <option v-for="option in options" :key="option.text">
      {{ option.text }}
    </option>
  </select>
  
</div>

  <br />
  <span>Selected: {{ selected }}</span>
    <h3>{{ response }}</h3>    
</div>
</template>

<script>
import axios from "axios";
export default {
  name: "HelloWorld",
  props: {
    msg: String,
  },
  data() {
    return {
      response: [],
      errors: [],
      selected: 'None',
      options: [
        { text: 'One' },
        { text: 'Two' },
        { text: 'Three'}
      ],
    };
  },
  methods: {
    callRestService() {
      axios
        .get(`images`)
        .then((response) => {
          // JSON responses are automatically parsed.
          this.response = response.data;
        })
        .catch((e) => {
          this.errors.push(e);
        });
    },
    callGetService() {
      axios
        .get(`images/0`)
        .then((response) => {
          // JSON responses are automatically parsed.
          this.response = response.data;
        })
        .catch((e) => {
          this.errors.push(e);
        });
    }

  }
};

/*Vue.createApp({
  data() {
    return {
      selected: 'A',
      options: [
        { text: 'One' },
        { text: 'Two' },
        { text: 'Three'}
      ]
    }
  }
}).mount('#v-model-select-dynamic')*/
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
