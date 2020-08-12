<template>
    <div class="container col-10 mt-5">

        <div class="mt-5" id="calendar">

        </div>
        <div class="card border-0">
            <div class="table-responsive">
                <table class="table align-items-center table-flush">
                    <thead style="Orange">
                        <tr>
                            <th scope="col">랭킹</th>
                            <th scope="col">사용자명</th>
                            <th scope="col">일지</th>
                            <th scope="col">평가</th>
                            <th scope="col">스터디 완료</th>
                            <th scope="col">총합</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(list, i) in RankData" :key="list.mid">
                            <td>{{i+1}}</td>
                            <td>{{list.user.nickname}}</td>
                            <td>{{list.diarypoint}}</td>
                            <td>{{list.evalpoint}}</td>
                            <td>{{list.endpoint}}</td>
                            <td>{{list.total}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</template>

<script>
    import axios from "axios";
    import constants from "../../lib/constants";
    import {
        Calendar
    } from '@fullcalendar/core';
    import dayGridPlugin from '@fullcalendar/daygrid';
    import interactionPlugin from '@fullcalendar/interaction';
    import listPlugin from '@fullcalendar/list';


    const SERVER_URL = "http://localhost:8080";


    export default {
        name: "Ranking",
        data: () => {
            return {
                events: [],
                profileInfo: [],
                RankData: [],
            };
        },
        components: {
            Calendar
        },

        created() {
            this.addprofileInfo();
            this.addRankData();
        },
        methods: {
            addprofileInfo() {
                if (this.$cookies.isKey("Auth-Token")) {
                    const token = this.$cookies.get("Auth-Token");
                    axios
                        .get(SERVER_URL + "/account/profile", {
                            params: {
                                Token: token,
                            },
                        })
                        .then((res) => {
                            this.profileInfo = res.data.object;
                            this.addStudyList();
                        })
                        .catch((err) => {
                            this.$router.push({
                                name: constants.URL_TYPE.ERROR.ERRORPAGE,
                                params: {
                                    code: err.response.data
                                },
                            });
                        });
                }
            },
            goMain(post_id) {
                this.$router.push({
                    name: constants.URL_TYPE.POST.MAIN,
                });
            },
            addRankData() {
                axios
                    .get(SERVER_URL + "/mileage/list")
                    .then((res) => {
                        this.RankData = res.data.object
                        console.log(RankData)
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            },

        },
    };
</script>

<style scoped>
    .boardtable:hover {
        background-color: #ddd;
    }

    .boardtablenotice {
        background-color: #FFF3E0;
    }

    .boardtablenotice:hover {
        background-color: #FFE0B2;
    }

    .blinking {
        -webkit-animation: blink 0.5s ease-in-out infinite alternate;
        -moz-animation: blink 0.5s ease-in-out infinite alternate;
        animation: blink 0.5s ease-in-out infinite alternate;
    }

    @-webkit-keyframes blink {
        0% {
            opacity: 0;
        }

        100% {
            opacity: 1;
        }
    }

    @-moz-keyframes blink {
        0% {
            opacity: 0;
        }

        100% {
            opacity: 1;
        }
    }

    @keyframes blink {
        0% {
            opacity: 0;
        }

        100% {
            opacity: 1;
        }
    }
</style>